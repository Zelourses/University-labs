default rel
global sepia_sse

%define c11 0.393
%define c12 0.769
%define c13 0.189
%define c21 0.349
%define c22 0.686
%define c23 0.168
%define c31 0.272
%define c32 0.534
%define c33 0.131
%define r1 0
%define r2 4
%define r3 8
%define r4 12
%define g1 16
%define g2 20
%define g3 24
%define g4 28
%define b1 32
%define b2 36
%define b3 40
%define b4 44


section .rodata
    red_coefs:      dd c11, c21, c31, c11, c21, c31, c11, c21, c31, c11
    green_coefs:    dd c12, c22, c32, c12, c22, c32, c12, c22, c32, c12
    blue_coefs:     dd c13, c23, c33, c13, c23, c33, c13, c23, c33, c13

section .text
; rsi - dst array
; src array looks like that: [ r1,r2,r3,r4, g1,g2,g3,g4, b1,b2,b3,b4 ](12)
; rdi show all it here:    rdi ^      rdi+16^      rdi+32^
;   formula: rdi+..
;   here is the table:
;           r1 | r2 | r3 | r4 | g1 | g2 | g3 | g4 | b1 | b2 | b3 | b4
;    rdi+..  0    4    8   12   16   20   24   28   32   36   40   44

; so, we do next- we copy r1 three times and then add r2 to the end to get r1,r1,r1,r2
; and repeat it with everything else(almost)

sepia_sse:
    sub rsp, 16             ; buffer for our things
    pxor  xmm6,xmm6        ; empty buffer for unpacking
    ;first register
    mov eax, dword[rdi+r1]
    mov dword[rsp],eax
    mov dword[rsp+4],eax
    mov dword[rsp+8],eax ; r1 | r1 | r1 | ..
    mov eax, dword[rdi+r2] ; r2
    mov dword[rsp+12], eax
    movups xmm0, [rsp]  ; r1 | r1 | r1 | r2
    ;second register
    mov eax, dword[rdi+g1] ; moving to g1
    mov dword[rsp],eax
    mov dword[rsp+4],eax
    mov dword[rsp+8],eax ; g1 | g1 | g1 | ..
    mov eax, dword[rdi+g2] ; g2
    mov dword[rsp+12], eax
    movups xmm1, [rsp]  ; g1 | g1 | g1 | g2
    ;third register
    mov eax, dword[rdi+b1]   ; moving to b1
    mov dword[rsp],eax
    mov dword[rsp+4],eax
    mov dword[rsp+8],eax ; b1 | b1 | b1 | ..
    mov eax, dword[rdi+b2] ; b2
    mov dword[rsp+12], eax
    movups xmm2, [rsp]  ; b1 | b1 | b1 | b2

    ; matrix operation    
    movups xmm3, [red_coefs]    ; xmm3 = c11 | c21 | c31 | c11
    movups xmm4, [green_coefs]  ; xmm4 = c12 | c22 | c23 | c12
    movups xmm5, [blue_coefs]   ; xmm5 = c13 | c23 | c33 | c13

    mulps xmm0, xmm3
    mulps xmm1, xmm4
    mulps xmm2, xmm5

    addps xmm0, xmm1
    addps xmm0, xmm2

    ;part where we destroy bad overflow arithmetic
    cvtps2dq xmm0, xmm0     ; float ->int32
    packusdw xmm0, xmm0     ; int32->int16  
    packuswb xmm0, xmm0     ; int16->int8
    punpcklbw xmm0, xmm6    ; int8->int16
    punpcklwd xmm0, xmm6    ; int16->int32
    cvtdq2ps xmm0, xmm0     ; int32->float

    movups [rsi], xmm0  ; a litlle part of it is ready

    mov eax, dword[rdi+r2] 
    mov dword[rsp],eax          ; r2 
    mov dword[rsp+4],eax        ; r2 | r2  
    mov eax, dword[rdi+r3]     ; change link to r3
    mov dword[rsp+8],eax        ; r2 | r2 | r3  
    mov dword[rsp+12], eax      ; r2 | r2 | r3 | r3
    movups xmm0, [rsp]          ; r2 | r2 | r3 | r3
    ;second register
    mov eax, dword[rdi+g2]      ; moving to g2
    mov dword[rsp],eax          ; g2 
    mov dword[rsp+4],eax        ; g2 | g2  
    mov eax, dword[rdi+g3]      ; change link to g3
    mov dword[rsp+8],eax        ; g2 | g2 | g3  
    mov dword[rsp+12], eax      ; g2 | g2 | g3 | g3
    movups xmm1, [rsp]          ; g2 | g2 | g3 | g3
    ;third register
    mov eax, dword[rdi+b2]      ; moving to b2
    mov dword[rsp],eax          ; b2 
    mov dword[rsp+4],eax        ; b2 | b2  
    mov eax, dword[rdi+b3]      ; change link to b3
    mov dword[rsp+8],eax        ; b2 | b2 | b3  
    mov dword[rsp+12], eax      ; b2 | b2 | b3 | b3
    movups xmm2, [rsp]          ; b2 | b2 | b3 | b3

    movups xmm3, [red_coefs+4]    ; xmm3 = c21 | c31 | c11 | c21
    movups xmm4, [green_coefs+4]  ; xmm4 = c22 | c32 | c12 | c22
    movups xmm5, [blue_coefs+4]   ; xmm5 = c23 | c33 | c13 | c23

    mulps xmm0, xmm3
    mulps xmm1, xmm4
    mulps xmm2, xmm5

    addps xmm0, xmm1
    addps xmm0, xmm2

    pxor  xmm6,xmm6  
    ;part where we destroy bad overflow arithmetic
    cvtps2dq xmm0, xmm0     ; float ->int32
    packusdw xmm0, xmm0     ; int32->int16  
    packuswb xmm0, xmm0     ; int16->int8
    punpcklbw xmm0, xmm6    ; int8->int16
    punpcklwd xmm0, xmm6    ; int16->int32
    cvtdq2ps xmm0, xmm0     ; int32->float

    movups [rsi+16], xmm0  ; a litlle part of it is ready, past it


    mov eax, dword[rdi+r3]       ; moving to r3
    mov dword[rsp],eax          ; r3 
    mov eax, dword[rdi+r4]      ; change link to r4
    mov dword[rsp+4],eax        ; r3 | r4  
    mov dword[rsp+8],eax        ; r3 | r4 | r4  
    mov dword[rsp+12], eax      ; r3 | r4 | r4 | r4
    movups xmm0, [rsp]          ; r3 | r4 | r4 | r4
    ;second register
    mov eax, dword[rdi+g3]      ; moving to g3
    mov dword[rsp],eax          ; g3 
    mov eax, dword[rdi+g4]      ; change link to g4
    mov dword[rsp+4],eax        ; g3 | g4  
    mov dword[rsp+8],eax        ; g3 | g4 | g4  
    mov dword[rsp+12], eax      ; g3 | g4 | g4 | g4
    movups xmm1, [rsp]          ; g3 | g4 | g4 | g4
    ;third register
    mov eax, dword[rdi+b3]      ; moving to b3
    mov dword[rsp],eax          ; b3 
    mov eax, dword[rdi+b4]      ; change link to b4
    mov dword[rsp+4],eax        ; b3 | b4  
    mov dword[rsp+8],eax        ; b3 | b4 | b4  
    mov dword[rsp+12], eax      ; b3 | b4 | b4 | b4
    movups xmm2, [rsp]          ; b3 | b4 | b4 | b4

    movups xmm3, [red_coefs+8]    ; xmm3 = c31 | c11 | c21 | c31
    movups xmm4, [green_coefs+8]  ; xmm4 = c32 | c12 | c22 | c23
    movups xmm5, [blue_coefs+8]   ; xmm5 = c33 | c13 | c23 | c33

    mulps xmm0, xmm3
    mulps xmm1, xmm4
    mulps xmm2, xmm5

    addps xmm0, xmm1
    addps xmm0, xmm2

    pxor  xmm6,xmm6  
    ;part where we destroy bad overflow arithmetic
    cvtps2dq xmm0, xmm0     ; float ->int32
    packusdw xmm0, xmm0     ; int32->int16  
    packuswb xmm0, xmm0     ; int16->int8
    punpcklbw xmm0, xmm6    ; int8->int16
    punpcklwd xmm0, xmm6    ; int16->int32
    cvtdq2ps xmm0, xmm0     ; int32->float

    movups [rsi+32], xmm0   ; all ready....

    add rsp,16              ; return to their place...

    ret

    
