global _start

section .data
    %define buffer_size 255
    %defstr buff_s buffer_size

    buffer: times 256 db 0
    error_message: db "ERROR: Probably you wrote more than ",buff_s," symbols",10, 0 
    line_message: db "Word to find: ",0
    not_found: db "We didn't found that word",10,0
    definition: db "Definition: ",0
section .text
%include "colon.inc"
%include "words.inc"

extern read_word
extern exit
extern print_string
extern print_newline
extern find_word
extern string_length

_start:
    push rdi
    push rdx
    mov rdi, buffer ;   куда писать
    mov rsi, buffer_size    ;   размер
    call read_word  ;   вызов функции чтения слова
    test rax, rax   ;   проверка на 0
    jz .error       ; прыжок на ошибку, завершение программы
    
    mov rdi, line_message
    mov rsi, 1          ;   номер потока куда писать
    call print_string   ; вывод надписи
    
    mov rdi, buffer
    mov rsi, 1  
    call print_string   ; вывод строки, которую написали
    call print_newline  ; перевод строки

    mov rdi, buffer
    mov rsi, next
    call find_word      ; поиск слова

    cmp rax, 0          ; проверка на то, что нашли.
    jz .not_found
    ; При успехе выводим описание:
    add rax, 8          ;Стоим на начале самого слова
    mov rbp, rax        ;Не сохраним r11, потому-что можем
    mov rdi, rax        ;Первый аргумент
    call string_length  ;теперь в rax лежит длина строки
    add rbp, rax        ; конец строки
    inc rbp             ; нуль-терминатор
    ;push r11
    ;Теперь по-хорошему мы находимся на начале следующей строки
    ;Поэтому, выводим текст
    mov rdi, definition
    mov rsi, 1
    call print_string
    mov rdi, rbp
    mov rsi, 1
    call print_string
    call print_newline

    call exit           ;выходим
    .error:
        mov rdi, error_message
        jmp error_write
    .not_found:
        mov rdi, not_found
error_write:
        mov rsi, 2  
        call print_string
        call exit
