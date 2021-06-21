`timescale 1ns / 1ps

module testNexysTopModule;


reg [15:0] SW;
reg clock;
reg reset;
reg start;
NexysTopModule topModule(
    .SW(SW),
    .CLK100MHZ(clock),
    .BTNR(reset),
    .BTNC(start)
    
);
endmodule
