
module sm_rom
#(
    parameter SIZE = 64
)
(
    input  [31:0] a,
    output [31:0] rd
);
    reg [31:0] rom [SIZE - 1:0];
    assign rd = rom [a];

    initial begin
        $readmemh ("D:\\dev\\labs\\CircuitDesign\\lab4.5\\program.hex", rom);
    end

endmodule