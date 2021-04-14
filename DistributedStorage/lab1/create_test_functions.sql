CREATE or replace FUNCTION my_function( x number )
return number IS
BEGIN
  RETURN x * 10;
END;/

CREATE or replace PROCEDURE procedure_three( x OUT NUMBER ) AS
BEGIN
  x:= 15;
END;/

CREATE or replace PROCEDURE procedure_two( x OUT NUMBER ) AS
BEGIN
  procedure_three( x );
  x:= x + 15;
END;/

CREATE or replace PROCEDURE procedure_one( x OUT NUMBER ) AS
  y number;
BEGIN
  procedure_two( y );
  x:= 20 + my_function( x ) + y;
END;/

CREATE or replace PROCEDURE main_proc AS
  x number;
BEGIN
  procedure_one( x );
END;/

select * from USER_DEPENDENCIES;

select * from ALL_DEPENDENCIES;