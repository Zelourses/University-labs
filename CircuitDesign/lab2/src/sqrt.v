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
localparam RUN = 1'b1;

reg state = IDLE;
reg [7:0] input_buffer, m, b, output_buffer;



always @(posedge clock) begin
    if(reset) begin
        state = IDLE;
        done <= 1'b0;
        input_buffer <= 0;
        m <= 1 << 6;
        output_buffer <= 0;
        output_x <= 0;
    end else begin
        case(state)
            IDLE: begin
                done <= 1'b0;
                output_x <= 0;
                if (start_in) begin
                    state <= RUN;
                    input_buffer <= input_x;
                    m <= 1 << 6;
                end
            end
            RUN: begin
                if (m != 0) begin
                    b = output_buffer | m;
                    output_buffer = output_buffer >> 1;
                    if (input_buffer >= b) begin
                        input_buffer <= input_buffer -b; //NOTE: may be need to change <= to = ? \/ too
                        output_buffer <= output_buffer | m; 
                    end 
                    m = m >>2;
                end else begin
                    state <= IDLE;
                    done <= 1'b1;
                    output_x <= output_buffer;
                end
            end
        endcase
    end
end
endmodule
