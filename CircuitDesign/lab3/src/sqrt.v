`timescale 1ns / 1ps

module sqrt(
    input clock,
    input reset,
    input start_in,
    input wire [7:0] input_x,

    output reg done,
    output reg [7:0] output_x
);

localparam IDLE = 1'b0;
localparam WORK = 1'b1;

reg [7:0] x_buffer;
reg [7:0] m;
reg [7:0] b;
reg [7:0] y_buffer;
reg state;

always @(posedge clock) begin
    if (reset) begin
        state <= IDLE;
        x_buffer <= 0;
        m <= 1 << 6;
        y_buffer <= 0;
        done <= 0;
    end else begin
    case (state)
        IDLE: begin
            done <= 0;
            if (start_in) begin
                state <= WORK;
                x_buffer <= input_x;
                m <= 1 << 6;
                output_x <= 0;
                y_buffer <= 0;
            end
        end
        WORK: begin
            if (m != 0) begin
            b = y_buffer | m;
            y_buffer = y_buffer >> 1;
            if (x_buffer >= b) begin
                x_buffer = x_buffer - b;
                y_buffer = y_buffer | m;
            end
            m = m >> 2;
            end else begin
                state <= IDLE;
                output_x <= y_buffer;
                done <=1;
            end
        end
    endcase
    end
end

endmodule
