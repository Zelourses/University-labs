`timescale 1ns / 1ps


module testSevenSegments;

reg [7:0] input_x;
reg clock, start;
wire done;
wire [15:0] result;
reg reset;
SevenSegments seven(
    .x(input_x),
    .clock(clock),
    .start(start),
    .reset(reset),
    
    .done(done),
    .y(result)
);


always #1 clock = ~clock;

initial begin
    clock = 1'b0;
    input_x = 8'd1;
    reset = 1;
    #5 start = 1'b1;
    reset = 0;
    #5 start = 1'b0;
    
    #100 $finish;
end
endmodule
