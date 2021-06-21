`timescale 1ns / 1ps


module tester;
reg clock, reset, start_in;
wire work, done;
reg [7:0] a,b;
wire [7:0] result;
threeModule tmodule(
    .clock(clock),
    .reset(reset),
    .start_in(start_in),
    .first(a),
    .second(b),
    
    .work(work),
    .done(done),
    .result(result)
);
initial begin
a <= 0;
b <= 9;
for (a = 1; a<2;a = a+1) begin
    clock = 0; reset = 1;
    
    #1 clock = ~clock;
    #1 clock = ~clock; reset = 0;
    #1 clock = ~clock; start_in = 1;
    #1 clock = ~clock; start_in = 0;
    #1 clock = ~clock;
    
    while (work) #1 clock = ~clock;
end 
$stop;
end
endmodule
