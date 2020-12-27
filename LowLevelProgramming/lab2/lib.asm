; FUNCTIONS FROM LAB1
global print_string:function
global read_word:function
global exit:function
global print_newline:function
global string_equals:function
global string_length:function
section .data
	buf : times 256 db 0
	number_chars: db '0123456789'

section .text

;Функции принимают аргументы в rdi, rsi, rdx, rcx, r8 и r9.

; Принимает код возврата и завершает текущий процесс
exit: 
    mov rax, 60 ; передаём код завершения операции
	xor rdi,rdi ; как сказано, быстрый метод очистки регистра (хотя и mov redi,0 должен сработать)
	syscall 	;всё как в примере hello_world
	

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
		xor rax, rax			; чистим регистр rax, который мы будем использовать в этой проге листнг 2-14
	.loop:						;считаем символы.....
		cmp byte [rdi+rax],0	; отнимаем, устанавливаются флаги. всё работает на прибавлении в rax
		je .end					;прыгаем отсюда, если получилось 0 (только если это нуль-терминатор) (нолик, короче)
		inc rax					;удивтельная команда, которую поддерживают современные процессоры.
		jmp .loop				;прыгаем обратно, считаем......
	.end:
		ret						;опа, закончили
		

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
; Теперь ещё принимает номер потока, куда писать
print_string:
	push rsi
	push rdi					;сохраняем значение rdi(аргумент) 
	call string_length			;т.к. мы знаем, что у нас есть в строке нуль-терминатор, ищем длину строки
	pop rsi						;тянем значение из rdi(потому-что в стеке) в rsi
	mov rdx, rax 				;заносим в регистр d данные из регистра a - длину строки
								;начинаем то, что было в hello_world
	mov rax, 1					;двигаем номер syscall в регистр 
			;(посмотреть на актуальные номера всегда можно по адресу /usr/include/x86_64-linux-gnu/asm/unistd_64.h)
	pop rdi					;дексриптор потока (stdout)
	syscall
	ret
		

; Принимает код символа и выводит его в stdout
print_newline:
    mov rdi, 10 ; можно было написать 0xA
	call print_char
	ret

print_char:
	mov rax,1 					;syscall number
	push rdi
	mov rdi, 1					;stdout descriptor
	mov rsi, rsp
	mov rdx,1
	syscall
	pop rdi
	ret
; Переводит строку (выводит символ с кодом 0xA)


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
	push r10
	mov r10,rsp
	mov rax,rdi
	mov rdi,10
	sub rsp,128
	dec r10
	mov byte[r10],0
.loop:
	dec r10
	xor rdx,rdx
	div rdi
	add rdx, '0'
	mov byte[r10],dl
	test rax,rax
	jnz .loop
	mov rdi,r10
	call print_string
	add rsp,128
	pop r10
	ret

; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
    xor rax,rax					;чистим rax
	test rdi,rdi				;устанавливаем флаги
	jns print_uint				;если число положительное-вывести
	push rdi					;выкидываем значение rdi
	mov rdi, '-'
	call print_char
	pop rdi
	neg rdi
	jmp print_uint

; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
    xor rax, rax	;	чистим rax, чтобы если что вернуть 0
	push r12
	push r13

	.loop:
		mov r12b, byte[rdi]
		mov r13b, byte[rsi]
		inc rdi
		inc rsi
		cmp r12b, r13b
		jne .false
		cmp r12b,0
		jne .loop
		inc rax		;попадаем только тогда, когда всё проверели
		;	проваливаемся дальше, и возвращаемся
	.false:
		pop r12
		pop r13
		ret

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    dec rsp
	mov rax, 0			;syscall read
	mov rdx,1			;1 символ
	mov rdi, 0			;откуда  - 0=stdin
	mov rsi,rsp			;указатель на то, куда всё двигать. rsi используется системными вызовами для ввода/вывода
	syscall
	mov rax, [rsp]
	inc rsp
	ret
	

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_word:
	push rbx 								; сохраняем rbx
	mov r8, rsi 							; сохраняем размер буфера
	mov r9, rdi 							; сохраняем адрес буфера
	xor rbx, rbx 							; обнуление счетчика длины
	xor rdi, rdi 							; откуда ввод - stdin
	mov rdx, 1 								; читаем 1 символ
.skip:
	xor rax, rax 							; sys_read
	mov rsi, r9 							; заносим адрес, куда будем считавать
	syscall
	cmp al, 0 								; сравниваем с концом строки
	je .finally 							; если равно, переходим к завершению
	cmp byte[r9], 0x21 					; сравниваем с кодом последнего 'плохого' символа
	jb .skip 								; если меньше, пропускаем его
	inc rbx 								; иначе учитываем
.read:
	xor rax, rax 							; код системного вызова sys_read
	lea rsi, [r9 + rbx] 					; записываем эффективный адрес в rsi (откуда читаем)
	syscall
	cmp byte [r9 + rbx], 0x21 				; сравниваем c последним не нужным символом
	jb .finally 							; если меньше (значит символ нам не нужен), -> завершаем чтение
	cmp r8, rbx 							; иначе проверяем умещается ли очередной символ
	jbe .exit 								; если нет, возвращаем 0
	inc rbx 								; иначе учитываем символ
	jmp .read 								; и читаем следующий
.finally:
	mov byte[r9 + rbx], 0 					; нуль-терминатор
	mov rdx, rbx 							; возвращаем длину строки
	mov rax, r9 							; возвращаем указатель на буфер
	pop rbx 								; восстанавливаем значение rbx
	ret
.exit:
	xor rdx, r8 							; записываем длину буфера (=сколько считали символов)
	xor rax, rax 							; возвращаем 0
	pop rbx 								; восстанавливаем значение rbx
	ret
 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
    mov rax, 0					;заносим в регистры
	mov rsi, 0 
	mov rcx, 0
	mov rdx, 0 
	mov r11, 10 

.loop:
	mov sil, [rdi+rcx] 						; перемещаем символ 
	cmp sil, '0' 							; цифра ли символ(нижняя)
	jl .return								; выход, если не цифра
	cmp sil, '9' 							; цифра ли символ(верхняя)
	jg .return 								; выход, если нет
	inc rcx
	sub sil, '0' 							; преобразование в число
	mul r11									; unsigned multiply
	add rax, rsi
	jmp .loop
.return:
	mov rdx, rcx
	ret




; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
    cmp byte[rdi], '-' 						; сравнение с минусом
	jne parse_uint 							; если нет, то парсим
	inc rdi 								; идём вперёд, если да
	call parse_uint 
	neg rax									; переворачиваем число
	inc rdx 								; счётчик++
	ret

; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
    push rdi			;указатель на строку
	push rsi			;указатель на буфер
	push rdx			;длина буфера
	
	call string_length	;получаем длину строки, она лежит в rax(регистр a)
	
	pop rdx				;возвращаем значение обратно
	pop rsi				;возвращаем значение обратно
	pop rdi				;возвращаем значение обратно
	
	cmp rax,rdx			;получаем разницу буфера. Если она равна 0, то нам не хватит места на нуль-терминатор
	jae .abort			;jump if equal - прыжок, если равны. Это плохо, ведь некуда положить нуль-терминатор
		push rsi
		.loop:
		mov al,byte[rdi]	;почему
		mov byte[rsi], al	;почему мне надо перемещать это в какой-то доп. буфер
		inc rdi
		inc rsi
		test al,al
		jnz .loop
		pop rax
		ret
	.abort:
		xor rax,rax		;чистим после себя rax, занятый string_length
		ret
