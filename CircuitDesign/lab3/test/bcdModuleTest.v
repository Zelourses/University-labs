`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 05/13/2021 09:42:50 PM
// Design Name: 
// Module Name: bcdModuleTest
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


module bcdModuleTest;
reg clock;
reg start;
reg reset;
reg [7:0] input_bytes;
wire done;
wire [3:0] hundreds, tens, ones;

bcd bcd(
    .binaryInput(input_bytes),
    .clock(clock),
    .start(start),
    .reset(reset),
    
    .done(done),
    .hundreds(hundreds),
    .tens(tens),
    .ones(ones)
);

always #1 clock = ~clock;


initial begin
    reset = 1'b1;
    clock = 1'b0;
    start = 1'b0;
    input_bytes = 8'b0;
    
    #10 reset = 1'b0;
    start = 1'b1;
    input_bytes = 162;
    
    #10 start = 1'b0;
    input_bytes = 8'b0;
    
    #100 $finish;
end
endmodule
