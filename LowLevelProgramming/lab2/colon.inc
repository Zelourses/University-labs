%ifndef colon_inc
%define colon_inc
; вставляется в начало файла. Поэтому "конец" листа всегда будет равен 0
%define next 0 

; сам макрос. принимает 2 значения, и вдобавок встроена проверка на строку
%macro colon 2
    %ifstr %1
        %2:
        dq next
        db %1,0
        %define next %2
    %else
        %error "First argument must be string"    
    %endif
    
%endmacro
%endif