`timescale 1ns / 1ps

module bcd(
    input [7:0] binaryInput,
    input clock,
    input start,
    input reset,
    
    output reg done,
    output reg [3:0] hundreds,
    output reg [3:0] tens,
    output reg [3:0] ones
);
integer i;

reg state;
reg [7:0] localInput;

localparam IDLE = 1'b0;
localparam WORK = 1'b1;

always @(posedge clock) begin
    if (reset) begin
        state <= IDLE;
        hundreds    <= 4'b0;
        tens        <= 4'b0;
        ones        <= 4'b0;
        localInput  <= 8'b0;
    end else begin
    case (state)
        IDLE: begin
            done <= 0;
            if (start) begin
                localInput  <= binaryInput;
                state       <= WORK;
            end
        end
        WORK: begin  
            hundreds    = 4'b0;
            tens        = 4'b0;
            ones        = 4'b0;            
            for (i=7; i>=0; i=i-1) begin
                if (hundreds >=5) hundreds  = hundreds + 3;
                if (tens     >=5) tens      = tens     + 3;
                if (ones     >=5) ones      = ones     + 3;
                
                hundreds = hundreds << 1;
                hundreds[0] = tens[3];
                tens = tens << 1;
                tens[0] = ones[3];
                ones = ones << 1;
                ones[0] = localInput[i];
            end
            state <= IDLE;
            done <= 1'b1;
        end
        default: state <= IDLE;
    endcase
    end
end
endmodule
