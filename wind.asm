.org 0x03F09478 //# Hijack Basement Door JAL by SpaceOmega5000
.word 0x804429F0

.org 0x03F429F0 //# Custom Multi-Effect Code by ThatCowGuy
MAIN:
ADDIU SP, SP, 0xFFE8
SW RA, 0x14(SP)
BEQ R0, R0, START
NOP

.byte 0x31
.byte 0x16
.halfword 0xFF38
.halfword 0x00C8
.halfword 0xFE0C
.halfword 0x012C
.halfword 0xFE0C
.halfword 0xFF9C
.byte 0x01
.byte 0x06
.halfword 0x0000
.halfword 0x00FA
.halfword 0x0000
.byte 0x1B
.byte 0x16
.halfword 0xFF38
.halfword 0x00C8
.halfword 0xFE0C
.halfword 0x012C
.halfword 0x012C
.halfword 0x02BC
.byte 0x01
.byte 0x06
.halfword 0x0014
.halfword 0x00FA
.halfword 0x0014
.halfword 0x6666 //# data_end
.byte 0xFF
.byte 0xFF

START:
LUI T1, @DATA
ADDI T1, T1, @DATA

LUI T3, @ADDR_MAP
LB T3, @ADDR_MAP(T3)

BOUNDARY_LOOP:
//=================================
    LH T2, 0x00(T1)  
    ADDI T8, R0, 0x6666
    BEQ T2, T8, BOUNDARY_LOOP_BREAK
    NOP
    
    LB T2, 0x00(T1)
    BNE T2, T3, WRONG_MAP_ID
    NOP
    
    ADDI T1, T1, 0x02
    
    LUI T0, @ADDR_XZY_POS
    ADDI T0, T0, @ADDR_XZY_POS
    ADDI T7, R0, 0x01 //# boolean: inside wind
    
    ADDI T9, R0, 0x00 //# i=0
    ADDI T8, R0, 0x03 //# max=3
    XYZ_LOOP:
        LWC1 F0, 0x00(T0)
        LH T2, 0x00(T1)
        MTC1 T2, F1
        CVT.S.W F1, F1
        SUB.S F1, F0, F1
        MFC1 T2, F1
        BLTZ T2, CLEAR_FLAG
        NOP
        
        LH T2, 0x02(T1)
        MTC1 T2, F1
        CVT.S.W F1, F1
        SUB.S F1, F0, F1
        MFC1 T2, F1
        BGTZ T2, CLEAR_FLAG
        NOP
        
        BEQ R0, R0, DONT_CLEAR_FLAG
        NOP
        
        CLEAR_FLAG:
        ADDI T7, R0, 0x00
        
        DONT_CLEAR_FLAG:
        ADDI T0, T0, 0x04
        ADDI T1, T1, 0x04
        
        ADDI T9, T9, 0x01 //# i++
        BEQ T9, T8, XYZ_LOOP_BREAK
        NOP
        BEQ R0, R0, XYZ_LOOP
        NOP
    XYZ_LOOP_BREAK:
    
    BNE T7, R0, GET_EFFECT
    NOP
    
    LB T2, 0x01(T1)
    ADD T1, T1, T2 //# Skip special entries
    ADDI T1, T1, 0x02
    
    BEQ R0, R0, BOUNDARY_LOOP
    NOP
    
    WRONG_MAP_ID:
    LB T2, 0x01(T1)
    ADD T1, T1, T2 //# Skip entire Data Block
    BEQ R0, R0, BOUNDARY_LOOP
    NOP
//=================================
BOUNDARY_LOOP_BREAK:

BEQ R0, R0, RETURN
NOP

GET_EFFECT:
LB T2, 0x00(T1)

ADDI T8, R0, 0x01 //# = Wind Hitbox
ADDI T1, T1, 0x02
BEQ T2, T8, APPLY_WIND
NOP

BEQ R0, R0, RETURN
NOP

APPLY_WIND:
LUI T0, @ADDR_XZY_VEL
ADDI T0, T0, @ADDR_XZY_VEL

ADDI T2, R0, @WIND_SPEED_CAP
MTC1 T2, F2
CVT.S.W F2, F2

ADDI T9, R0, 0x00 //# i=0
ADDI T8, R0, 0x03 //# max=3
WIND_LOOP:
    LWC1 F0, 0x00(T0)
    LH T2, 0x00(T1)
    MTC1 T2, F1
    CVT.S.W F1, F1
    ADD.S F0, F0, F1
    
    SUB.S F1, F0, F2 //# F1 = spd - cap >>> has to be < 0
    MFC1 T2, F1
    BGTZ T2, POS_SPEED_CAP
    NOP
    ADD.S F1, F0, F2 //# F1 = spd + cap >>> has to be > 0
    MFC1 T2, F1
    BLTZ T2, NEG_SPEED_CAP
    NOP
    
    MFC1 T2, F0
    BEQ R0, R0, APPLY
    NOP
    
    POS_SPEED_CAP:
    MFC1 T2, F2
    BEQ R0, R0, APPLY
    NOP
    
    NEG_SPEED_CAP:
    NEG.S F2, F2
    MFC1 T2, F2
    NEG.S F2, F2
    BEQ R0, R0, APPLY
    NOP
    
    APPLY:
    SW T2, 0x00(T0)
    
    ADDI T0, T0, 0x04
    ADDI T1, T1, 0x02
    
    ADDI T9, T9, 0x01 //# i++
    BEQ T9, T8, BOUNDARY_LOOP
    NOP
    BEQ R0, R0, WIND_LOOP
    NOP

RETURN:
LW RA, 0x14(SP)
JR RA
ADDIU SP, SP, 0x18

[WIND_SPEED_CAP]: 0x0258 //# 600
[ADDR_XZY_POS]: 0x8038C5A0 //# remember 0x10 offset
[ADDR_XZY_VEL]: 0x8038C4B8
[ADDR_MAP]: 0x8038E8F5 //# this is only 1 Byte
[DATA]: 0x80442A00
