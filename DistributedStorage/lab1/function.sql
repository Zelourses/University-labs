declare
    type list_of_funcs is table of VARCHAR(255);
    procedure calling(func_name varchar, exclude list_of_funcs) as
        exclude_new list_of_funcs := exclude;
        begin
            exclude_new.extend;
            exclude_new(exclude_new.COUNT) := func_name;
            DBMS_OUTPUT.PUT_LINE('Proc '||func_name);
            for i in (
                select referenced_name
                from USER_DEPENDENCIES
                where NAME = func_name
                  and REFERENCED_OWNER <> 'SYS'
            ) loop
                    DBMS_OUTPUT.PUT_LINE('   call '|| i.REFERENCED_NAME);
            end loop;
            DBMS_OUTPUT.PUT_LINE('END');
            for i in (
                select referenced_name
                from USER_DEPENDENCIES
                where NAME = func_name
                  and REFERENCED_OWNER <> 'SYS'
            ) loop
                if i.REFERENCED_NAME not member of exclude then
                    calling(i.REFERENCED_NAME, exclude);
                    end if;
            end loop;
        end;
begin
    calling('MAIN_PROC',list_of_funcs() );
end;