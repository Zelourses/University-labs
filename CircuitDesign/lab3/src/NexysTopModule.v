`timescale 1ns / 1ps

module NexysTopModule(
    input [15:0] SW,
    input CLK100MHZ,
    input BTNR,
    input BTNC,
    
    output LED16_R,
    output[15:0] LED,
    output reg [7:0] cat_arr,
    output reg [7:0] AN
);

reg [7:0] result_module;
reg [7:0] input_a;
reg [7:0] input_b;
wire [7:0] topModuleResult;
reg start = 1'b0;
wire done;
topModule topModule(
    .clock(CLK100MHZ),
    .reset(BTNR),
    .start(start),
    
    .input_a(input_a),
    .input_b(input_b),
    
    .done(done),
    .result(topModuleResult)
);
assign LED[7:0] = topModuleResult;


reg [15:0] segments;
wire [15:0] tmpSegments;
wire segmentsDone;
SevenSegments SevenSegments(
    .x(topModuleResult),
    .clock(CLK100MHZ),
    .start(done),
    .reset(BTNR),
    .done(segmentsDone),
    .y(tmpSegments)
);

reg BTNC_prev;
integer time_passed = 0;
reg segmentToShow = 0;


always @(posedge CLK100MHZ) begin
    if (segmentsDone) begin
        segments <= tmpSegments;
        segmentToShow <= 0;
    end else if (time_passed > 400000) begin
        cat_arr <= segments[7:0];
        segments <= {segments[7:0], segments[15:8]};
        /*AN[0+segmentToShow] <= 0;
        AN[0+~segmentToShow] <=1;*/
        AN <= {6'b111111,~segmentToShow, segmentToShow}; 
        //cat_arr <= 8'b11110000;
        time_passed <= 0;
        segmentToShow <= ~segmentToShow;
    end else begin
         time_passed <= time_passed+1;
     end
     
    if (BTNR) begin
        start <= 1'b0;
        input_a <= 8'b0;
        input_b <= 8'b0;
    end else begin
        if (BTNC & ~BTNC_prev) begin
            input_a <= SW[7:0];
            input_b <= SW[15:8];
            start <= 1'b1;
        end else begin
            start <= 1'b0;
        end 
    end
end
always @(posedge CLK100MHZ) begin
    BTNC_prev <= BTNC;
end

endmodule
