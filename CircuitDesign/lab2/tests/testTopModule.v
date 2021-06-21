`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 05/06/2021 03:35:04 PM
// Design Name: 
// Module Name: testTopModule
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


module testTopModule;
reg clk;
reg start_in;
reg reset;
reg [7:0] input_a;
reg [7:0] input_b;
wire done;
wire [7:0] output_a;
topModule topModule(
    .clock(clk),
    .reset(reset),
    .start(start_in),
    
    .input_a(input_a),
    .input_b(input_b),
    
    .done(done),
    .result(output_a)
);

always  #5 clk = ~clk;

initial begin
    reset = 1'b1;
    clk = 1'b0;
    start_in = 1'b0;
    input_a = 8'b0;
    input_b = 8'b0;
    
    #10 reset = 1'b0;
    start_in = 1'b1;
    input_a = 25;
    input_b = 4;
    
    #10 start_in = 1'b0;
    input_a = 8'b0;
    input_b = 8'b0;
    
    #200 $finish;
end

endmodule
