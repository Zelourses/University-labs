`timescale 1ns / 1ps

module topModule(
    input clock,
    input reset,
    input start,
    
    input [7:0] input_a,
    input [7:0] input_b,
    
    output done,
    output [7:0] result
);

wire sqrt1_done;
wire [7:0] sqrt1_result;
wire [7:0] sqrt_plus_result;
wire sqrt_plus_done;
reg [7:0] input_b_reg;

always @(posedge clock) begin
    if (start) begin
        input_b_reg <= input_b;
    end
end 

sqrt sqrt1(
    .clock(clock),
    .reset(reset),
    .start_in(start),
    .input_x(input_a),
    
    .done(sqrt1_done),
    .output_x(sqrt1_result)
);

assign sqrt_plus_result = sqrt1_result+input_b_reg;
//assign sqrt_plus_done = sqrt1_done;
sqrt sqrt2(
    .clock(clock),
    .reset(reset),
    .start_in(sqrt1_done),
    .input_x(sqrt_plus_result),
    
    .done(done),
    .output_x(result)
);


endmodule
