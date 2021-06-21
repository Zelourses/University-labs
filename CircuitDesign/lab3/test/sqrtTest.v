`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 05/06/2021 03:17:55 PM
// Design Name: 
// Module Name: sqrtTest
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module sqrtTest;
reg clk;
reg start_in;
reg reset;
reg [7:0] input_x;
wire done;
wire [7:0] output_x;
sqrt sqrt(
    .clock(clk),
    .reset(reset),
    .start_in(start_in),
    .input_x(input_x),
    
    .done(done),
    .output_x(output_x)
);

always  #5 clk = ~clk;

initial begin
    reset = 1'b1;
    clk = 1'b0;
    start_in = 1'b0;
    input_x = 8'b0;
    
    #10 reset = 1'b0;
    start_in = 1'b1;
    input_x = 8;
    
    #10 //start_in = 1'b0;
    //input_x = 1'b0;
    
    #100 $finish;
end
endmodule
