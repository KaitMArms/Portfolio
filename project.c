#include "spimcore.h"

/*
Halt Protocol Scenarios
    - An illegal address in encountered
    - Jumping to an address that is not word-aligned (aka a multiple of 4)
    - The address of lw or sw is not word-aligned
    - Accessing data or jumping to an address that is beyond the memory
*/

/* ALU */
/* 10 Points */
void ALU(unsigned A,unsigned B,char ALUControl,unsigned *ALUresult,char *Zero)
{
    printf("%s\n", "ALU Entered");
    // Internal Variables
    unsigned Z;

    //Implement opperations on inputs A & B according to ALUControl
    if(ALUControl == 0b000)
    {
        Z = A + B;
    }
    else if(ALUControl == 0b001)
    {
        Z = A - B;
    }
    else if(ALUControl == 0b010)
    {
        if((int)A < (int)B)
        {
            Z = 1;
        }
        else
        {
            Z = 0;
        }
    }
    else if(ALUControl == 0b011)
    {
        if(A < B)
        {
            Z = 1;
        }
        else
        {
            Z = 0;
        }
    }
    else if(ALUControl == 0b100)
    {
        Z = A & B;
    }
    else if(ALUControl == 0b101)
    {
        Z = A | B;
    }
    else if(ALUControl == 0b110)
    {
        Z = B << 16;
        printf("%x", Z);
    }
    else if(ALUControl == 0b111)
    {
        Z = ~A;
    }

    // Set ALUResult & Zero
    *ALUresult = Z;// Output result(z) to ALUresult
    // Zero = 1 if result = 0, else Zero = 0
    if(*ALUresult == 0) 
    {
        *Zero = 1;
    }
    else
    {
        *Zero = 0;
    }
    printf("%s\n", "ALU left");
}

/* instruction fetch */
/* 10 Points */
int instruction_fetch(unsigned PC,unsigned *Mem,unsigned *instruction)
{
    // Fetch the instruction by PC from Mem and write it to instruction 
        // Mem is populated array
        // PC is Current index to use for instruction memory
        // Check for word alignment
        // Use PC >> 2 to get actual location
    // Return 1 if halt condition occurs, else return 0
    printf("%s\n", "Fetch Entered");
    // Gets Location
    //PC = PC >> 2;

    // Checks that location is valid
    if((PC % 4) == 0 && &instruction[PC>>2] != NULL)
    {
        // Location is valid so proceed
        printf("%s\n", "Fetch left");
        *instruction = Mem[PC>>2];
        return 0;
    }
    else
    {
        // Location is invalid so Halt
        printf("%s", "Halt in fetch\n");
        return 1;
    }
    
}

/* instruction partition */
/* 10 Points */
void instruction_partition(unsigned instruction, unsigned *op, unsigned *r1,unsigned *r2, unsigned *r3, unsigned *funct, unsigned *offset, unsigned *jsec)
{
    printf("%s\n", "Part Entered");
    // Instruction Parts
   *op = 0b11111100000000000000000000000000 & instruction;
   *r1 = 0b00000011111000000000000000000000 & instruction;
   *r2 = 0b00000000000111110000000000000000 & instruction;
   *r3 = 0b00000000000000001111100000000000 & instruction;
   *funct = 0b00000000000000000000000000111111 & instruction;
   *offset = 0b00000000000000001111111111111111 & instruction;
   *jsec = 0b00000011111111111111111111111111 & instruction;

    // shift out 0s for the ones that end in 0s
   *op = *op >> 26;
   *r1 = *r1 >> 21;
   *r2 = *r2 >> 16;
   *r3 = *r3 >> 11;
   printf("%s\n", "part left");
}



/* instruction decode */
/* 15 Points */
int instruction_decode(unsigned op, struct_controls *controls)
{
    // Decode instruction using op
        // Based on op, set the control signal in the controls structure (stored as char)
    // Assign values of the control signals to variables in structure control in spimcore.h 
        // MemRead, MemWrite, RegWrite
            // 1 = enabled
            // 0 = disabled
            // 2 = ignore
        // RegDst, Jump, Branch, MemtoReg, ALUsrc
            // 0 or 1 = indicates path of multiplexer
            // 2 = ignore
        //ALUOp is 3 bit signal 
            // 000 = add or don't care
            // 001 = subtract
            // 010 = set less then
            // 011 = set less then unsigned
            // 100 = AND op
            // 101 = OR op
            // 110 = shift extended_value by 16 bits
            // 111 = R-Type instruction
    // Return 1 if halt condition occurs, else return 0
    printf("%s\n", "decode Entered");
    // Control Unit Set Up
    if(op == 0b000000)
    {
        // R-Type
        controls->ALUOp = 0b111;
        controls->ALUSrc = 0;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 1;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b001000)
    {
        // addi
        controls->ALUOp = 0b000;
        controls->ALUSrc = 1;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b001100)
    {
        // and
        controls->ALUOp = 0b111;
        controls->ALUSrc = 0;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b000101)
    {
        // or
        controls->ALUOp = 0b111;
        controls->ALUSrc = 0;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b100011)
    {
        // lw
        controls->ALUOp = 0b000;
        controls->ALUSrc = 1;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 1;
        controls->MemtoReg = 1;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b101011)
    {
        // sw
        controls->ALUOp = 0b000;
        controls->ALUSrc = 1;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 1;
        controls->RegDst = 0;
        controls->RegWrite = 0;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b000100)
    {
        // beq
        controls->ALUOp = 0b001;
        controls->ALUSrc = 0;
        controls->Branch = 1;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 2;
        controls->RegWrite = 0;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b001111)
    {
        // load upper immediate
        controls->ALUOp = 0b110;
        controls->ALUSrc = 1;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0; 
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b001010)
    {
        // set less than immediate
        controls->ALUOp = 0b010;
        controls->ALUSrc = 1;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if(op == 0b001011)
    {
        // set less than immediate unsigned
        controls->ALUOp = 0b011;
        controls->ALUSrc = 1 ;
        controls->Branch = 0;
        controls->Jump = 0;
        controls->MemRead = 0;
        controls->MemtoReg = 0;
        controls->MemWrite = 0;
        controls->RegDst = 0;
        controls->RegWrite = 1;
        printf("%s\n", "decode left");
        return 0;
    }
    else if (op == 0b000010)
    {
        // jump
        controls->ALUOp = 0b000;
        controls->ALUSrc = 2;
        controls->Branch = 0;
        controls->Jump = 1;
        controls->MemRead = 0;
        controls->MemtoReg = 2;
        controls->MemWrite = 0;
        controls->RegDst = 2;
        controls->RegWrite = 0;
        printf("%s\n", "decode left");
        return 0;
    }
    else
    {
        // OP not valid
        printf("%s", "Halt in decode\n");
        return 1;
    }
}

/* Read Register */
/* 5 Points */
void read_register(unsigned r1,unsigned r2,unsigned *Reg,unsigned *data1,unsigned *data2)
{
    printf("%s\n", "read entered");
    // Read registers addressed by r1 & r2 from read
        // reg is an array containing the register file
    // Write the read values to data1 & data2
    *data1 = Reg[r1];
    *data2 = Reg[r2];
    printf("%s\n", "read left");
}


/* Sign Extend */
/* 10 Points */
void sign_extend(unsigned offset,unsigned *extended_value)
{
    // Assign the sign-extended value of offset to extend_value
        // 16th bit is the sign bit
        // if upper order 16 bits is all 0s, no change is needed, only if there are 1s

    // Looks at sign bit to determine pos or neg
    printf("%s\t%u\n", "sign entered",offset);
    if(offset >> 15 == 1)
    {
        *extended_value = 0b11111111111111110000000000000000 | offset;
    }
    else
    {
        // Sets upper half to 0
        *extended_value = 0b00000000000000001111111111111111 & offset;
    }
    printf("%s\t%u\n", "sign left", *extended_value);
}

/* ALU operations */
/* 10 Points */
int ALU_operations(unsigned data1,unsigned data2,unsigned extended_value,unsigned funct,char ALUOp,char ALUSrc,unsigned *ALUresult,char *Zero)
{
    // Apply ALU operations on data1 and either data2 to or extended_value
        // ALUSrc determines whether data2 or extended_value is used
    // Operation performed based on ALUOp & funct
        // Set parameters for A, B, & ALUControl
        // If R-Type instructions look at funct
        // At end call ALU function
    // Apply function ALU()
    //ALU();
    // Output result to ALUresult
    // Return 1 if a halt condition occurs, else return 0
    printf("%s\n", "ALUOp entered");
    // Variables
    unsigned A;
    unsigned B;
    unsigned ALUControl;

    // Uses ALUSrc to set A, B & ALUControl
    if(ALUSrc == 0)
    {
        // ALUSrc indicates either an R-Type or Beq so use data1 & data2
        A = data1;
        B = data2;

        // Check to figure out control
        if(ALUOp == 0b111)
        {
            // Sets ALUControl off of funct
            if(funct == 0b000000)
            {
                // Shift Logical Left
                ALUControl = 0b110;
            }
            else if(funct == 0b100000)
            {
                // Add
                ALUControl = 0b000;
            }
            else if(funct == 0b1000011)
            {
                // Addu
                ALUControl = 0b000;
            }
            else if(funct == 0b100010)
            {
                // Subtract
                ALUControl = 0b001;
            }
            else if(funct == 0b100011)
            {
                // Subu
                ALUControl = 0b001;
            }
            else if(funct == 0b100100)
            {
                // And
                ALUControl = 0b100;
            }
            else if(funct == 0b100101)
            {
                // Or
                ALUControl = 0b101;
            }
            else if(funct == 0b101010)
            {
                // Set Less Than
                ALUControl = 0b010;
            }
            else if(funct == 0b101011)
            {
                // Sltu
                ALUControl = 0b011;
            }
        }
        else
        {
            // Is Beq
            ALUControl = ALUOp; // Sets to subtract
        }
    }
    else if (ALUSrc == 1)
    {
        // ALUSrc indicates I-type
        A = data1;
        B = extended_value;
        ALUControl = ALUOp;

        printf("A = %u B = %u ALUC = %u", A, B, ALUControl);
    }
    else
    {
        printf("%s", "Halt in ALUOp\n");
        // ALUSrc is ignored so ALU not used. Return Halt
        return 1;
    }

    // Calls ALU
    ALU(A, B, ALUControl, ALUresult, Zero);
    printf("%s\n", "ALUOP left");
    return 0;
}

/* Read / Write Memory */
/* 10 Points */
int rw_memory(unsigned ALUresult,unsigned data2,char MemWrite,char MemRead,unsigned *memdata,unsigned *Mem)
{
    // Use the value  of MemWrite or MemRead to determine if a memory write operation or memory read or niether operation is occuring
    
    // If reading from memory, read the content of the memory location addressed by ALUresult to memdata
    // If writing to memory, read the value of data2 to the memory location
        // Mem is the memory array
        // if MemWrite = 1, check word alignment & write into memory
        // if MemRead = 1, Check word alignment and read from memory
    // Return 1 if halt condition occurs, else return 0
    printf("%s\n", "rw entered");
    if(MemWrite == 1)
    {
        if((ALUresult % 4) == 0)
        {
            Mem[ALUresult >> 2] = data2;
            printf("%s\n", "rw left");
            return 0;
        }
        else
        {
            printf("%s", "Halt in RW\n");
            return 1;
        }
    }
    else if(MemRead == 1)
    {
        if((ALUresult % 4) == 0)
        {
            printf("%s\n", "rw left");
            *memdata = Mem[ALUresult >> 2];
            return 0;
        }
        else
        {
            printf("%s", "Halt in RW\n");
            return 1;
        }
    }
    else
    {
        // Neither occur
        return 0;
    }
}


/* Write Register */
/* 10 Points */
void write_register(unsigned r2,unsigned r3,unsigned memdata,unsigned ALUresult,char RegWrite,char RegDst,char MemtoReg,unsigned *Reg)
{
    printf("%s\n", "write entered");
    // Write the data (ALUresult or memdata), to a register (Reg)addressed by r2 or r3
    // if RegWrite = 1, place write data into the register specificied
    if(RegWrite == 1 && MemtoReg == 1) 
    {
        // if RegWrite & MemtoReg == 1, data is coming from memory
        if(RegDst == 1)
        {
            Reg[r3] = memdata;
        }
        else if(RegDst == 0)
        {
            Reg[r2] = memdata;
        }
    }
    else if (RegWrite == 1 && MemtoReg == 0)
    {
        // if RegWrite = 1 & MemtoReg = 0, data is coming from ALU_result
        if(RegDst == 1)
        {
            Reg[r3] = ALUresult;
        }
        else if(RegDst == 0)
        {
            Reg[r2] = ALUresult;
        }
    }
    printf("%s\n", "write left");
}

/* PC update */
/* 10 Points */
void PC_update(unsigned jsec,unsigned extended_value,char Branch,char Jump,char Zero,unsigned *PC)
{
    // Variables
    unsigned tmp;

    // Update program counter
        // Normal case: PC = PC + 4
        // Take care of Branch & Jump
            // Zero: branch taken or not
            // Jump: Left shift bits of jsec by 2 and use upper 4 bits of PC
    printf("%s\n", "pc entered");
    printf("Begin PC = %d\n", *PC);
    if(Jump == 1)
    {
        // Sets PC & jsec so they can be combine
        jsec = jsec << 2;
        *PC = *PC + 4;
        // How would I could use upper 4 bits of PC?
        // Takes top 4 bits of PC after adding 4  and adds it to jsec with mask so it is added to the front, then two zeros placed after the two concatinated 
        
        *PC = ((0b00001111111111111111111111111111 & jsec) + (0b11110000000000000000000000000000 & *PC));
    }
    else if(Branch == 1)
    {
        if(Zero == 1)
        {
            // Branch branches
            *PC = ((*PC + 4) + (extended_value<<2));
        }
        else if(Zero == 0)
        {
            // Branch does not branch
            *PC = *PC + 4;
        }
    }
    else
    {
        *PC = *PC + 4;
    }
    printf("End PC = %d\n", *PC);
    printf("%s\n", "pc left");
}

