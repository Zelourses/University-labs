
global find_word:function
section .text
extern string_equals
; принимает 2 аргумента, указатель на строку и указатель на последнее слово в словаре
find_word:
    mov rdx, rdi     ; сохраняем адрес строки
.loop:
    mov rcx, rsi
    cmp rsi, 0      ; проверка на нулевой указатель
    jz .false
    mov rdi, rdx
    add rsi, 8
    call string_equals
    mov rsi, rcx    ;   возвращаем на место указатель на строку(потому-что мы его двигали)
    cmp rax, 0      ;   проверяем на 0
    jnz .true       ;   только в случае, если строки равны
    mov rsi, [rsi]  ;   прыгаем туда, где находится следующий элемент
    jmp .loop

.false:
    mov rax, 0
    ret
.true:
    mov rax, rsi
    ret
