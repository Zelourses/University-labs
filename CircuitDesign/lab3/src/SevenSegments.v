`timescale 1ns / 1ps


module SevenSegments(
    input [7:0] x,
    input clock,
    input start,
    input reset,
    
    output reg done,
    output reg [15:0] y
);
/*wire [3:0] tens, ones;
wire bcdDone;

bcd bcd(
    .binaryInput(x),
    .clock(clock),
    .start(start),
    .reset(reset),
  
    .done(bcdDone),
    .hundreds(), // empty, yeah
    .tens(tens),
    .ones(ones)
);*/

reg state = 0;


localparam IDLE = 1'b0;
localparam WORK = 1'b1;

localparam ZERO = 4'b0000;
localparam ONE = 4'b0001;
localparam TWO = 4'b0010;
localparam THREE = 4'b0011;
localparam FOUR = 4'b0100;
localparam FIVE = 4'b0101;
localparam SIX = 4'b0110;
localparam SEVEN = 4'b0111;
localparam EIGHT = 4'b1000;
localparam NINE = 4'b1001;



always @(posedge clock) begin
    if (reset) begin
        done <=0;
        y <= 0;
    end else begin
        case (state)
            IDLE : begin
                done <= 0;
                if (start) begin
                    state <= WORK;
                    y <= 0;
                end
            end
            
            WORK: begin
                /*if (bcdDone) begin
                    *//*y[15:8] <= tens;
                    y[7:0] <= ones;*//*
                    case(tens) 
                        ZERO:   y[15:8] <= 8'b11000000;
                        ONE:    y[15:8] <= 8'b11111001;
                        TWO:    y[15:8] <= 8'b10100100;
                        THREE:  y[15:8] <= 8'b10110000;
                        FOUR:   y[15:8] <= 8'b10011001;
                        FIVE:   y[15:8] <= 8'b10010010;
                        SIX:    y[15:8] <= 8'b10000010;
                        SEVEN:  y[15:8] <= 8'b11111000;
                        EIGHT:  y[15:8] <= 8'b10000000;
                        NINE:   y[15:8] <= 8'b10010000;
                    endcase
                    case(ones) 
                        ZERO:   y[7:0] <= 8'b11000000;
                        ONE:    y[7:0] <= 8'b11111001;
                        TWO:    y[7:0] <= 8'b10100100;
                        THREE:  y[7:0] <= 8'b10110000;
                        FOUR:   y[7:0] <= 8'b10011001;
                        FIVE:   y[7:0] <= 8'b10010010;
                        SIX:    y[7:0] <= 8'b10000010;
                        SEVEN:  y[7:0] <= 8'b11111000;
                        EIGHT:  y[7:0] <= 8'b10000000;
                        NINE:   y[7:0] <= 8'b10010000;
                    endcase
                    done <= 1;
                    state <= IDLE;
                end*/
                
                y[7] <= 1;
                y[6] <= ~x[1] & ( ~x[2] | ~x[3] & ~x[4]);
                y[5] <= ~x[1] & ( ~x[2] | ~x[3] & ~x[4]);
                y[4] <= 1;
                y[3] <= 1;
                y[2] <= 1;
                y[1] <= 1;
                y[0] <= 1;
                
                y[15] <= ( x[3] | x[5] ) & ( x[2] | ~x[4] ) & ( ~x[3] | ~x[5] ) & ( ~x[2] | x[4]);
                y[14] <= x[2] & x[3] & x[4] & x[5] | ~x[2] & x[3] & ( ~x[4] & x[5] | x[4] & ~x[5] ) | x[1];
                y[13] <= x[2] & x[3] & ~x[4] & ~x[5] | ~x[2] & ~x[3] & x[4] & ~x[5];
                y[12] <= ( x[2] | ~x[4] | x[3] & x[5] ) & ( ~x[3] | ~x[5] | ~x[2] & x[4] ) & ( ~x[2] | x[4] ) & ( x[3] | x[5] );
                y[11] <= ~x[2] & x[3] & ~x[4] | x[2] & x[3] & x[4] | x[5];
                y[10] <= x[2] & x[3] & ~x[4] | ~x[2] & ~x[3] & ( x[4] | x[5] ) | x[4] & x[5] & ( ~x[2] | ~x[3] );
                y[9] <= ~x[1] & ~x[2] & ~x[3] & ~x[4] | ~x[2] & x[3] & x[4] & x[5] | x[2] & ~x[3] & x[4];
                y[8] <= 1;
            end 
            
            default: state <= IDLE;
        endcase
    end
end


endmodule
