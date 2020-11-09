//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//# Labels and Constants
[WIND_SPEED_CAP]:       0x0258      //# = 600
[ADDR_XZY_POS_1]:       0x8037C5A0  //# remember 0x10 offset
[ADDR_XZY_POS_2]:       0x8037C5B0
[ADDR_XZY_VEL]:         0x8037C4B8
[ADDR_MAP]:             0x8037E8F5  //# 1B
[DATA]:                 0x8046B8D0
[BUTTON_ARR_1]:         0x802812B8  //# 1B (ABZS and DPad-UDLR)
[BUTTON_ARR_2]:         0x802812B9  //# 1B (??LR and C-UDLR)

[HIJACK_JAL_LOC]:       0x8024E7D4
[HIJACK_RA]:            0x8023E000

// 0x03F42930 = 0x80442930 in ROM - space: 0x670 (1648 Bytes)
// 0x03F6B8D0 = 0x8046B8D0 in ROM - space: 0x1D0 (464 B Bytes)
//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.org 0x03F09478 //# BasementDoor Hijack by SpaceOmega5000
.word 0x80442930
//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.org 0x03F42930 //# Runtime JAL-Hijack by ThatCowGuy
                //# with help from Wedarobi
ADDIU SP, SP, 0xFFE8
SW T0, 0x14(SP)
SW T1, 0x10(SP)
LUI T0, @HIJACK_JAL_LOC
ORI T0, T0, @HIJACK_JAL_LOC
LUI T1, 0x0C11 //# HEX version of JAL 0x80442960
ORI T1, T1, 0x0A58
SW T1, 0x00(T0)
LW T1, 0x10(SP)
LW T0, 0x14(SP)
JR RA
ADDIU SP, SP, 0x18
//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.org 0x03F42960 //# Multi-Effect ASM by ThatCowGuy
                //# Code is @0x80442960 (0x7C50 0000 off)
MAIN:
//# store some regs for later restoring
ADDIU SP, SP, 0xFFE8
SW RA, 0x14(SP)
SW A0, 0x10(SP)
SW A1, 0x0C(SP)
SW A2, 0x08(SP)
SW A3, 0x04(SP)

//# init DATA-pointer
LUI T1, @DATA
ORI T1, T1, @DATA
//# read current-Room-ID
LUI T2, @ADDR_MAP
ORI T2, T2, @ADDR_MAP
LB T2, 0x00(T2)

EFFECT_LOOP:
//#==========================================
    //# Check for Terminator
    LH T3, 0x00(T1)  
    ORI T8, R0, 0x6666
    BEQ T3, T8, RETURN
    NOP
    
    //# load current-Room-ID
    LB T3, 0x00(T1)
    //# load struct-size
    LB T4, 0x01(T1)
    //# load effect-ID
    LB T5, 0x02(T1)
    //# load special-size
    LB T6, 0x03(T1)

    //# compare Room-IDs
    BNE T2, T3, WRONG_MAP_ID
    NOP

    ADDI T1, T1, 0x04
    
    //# load &player.pos
    LUI A0, @ADDR_XZY_POS_1
    ORI A0, A0, @ADDR_XZY_POS_1
    //# load &min_bound
    OR A1, R0, T1 
    ADDI T1, T1, 0x0C
    //# load &max_bound
    OR A2, R0, T1 
    ADDI T1, T1, 0x0C
    //# perform box-check: V0 = *A0 in [*A1:*A2]
    JAL 0x802584FC
    NOP
    BNE V0, R0, GET_EFFECT
    NOP

    //# skip special entries
    ADD T1, T1, T6
    //# continue
    BEQ R0, R0, EFFECT_LOOP
    NOP
    
    WRONG_MAP_ID:
    //# Skip this entire struct
    ADD T1, T1, T4
    //# continue
    BEQ R0, R0, EFFECT_LOOP
    NOP

    GET_EFFECT: //# NO FAILSAFE to save some space
    //# if effect-ID = 1 = Wind
    ORI T8, R0, 0x01
    BEQ T5, T8, APPLY_WIND
    NOP

    APPLY_WIND:
    //# load &player.vel as dest
    LUI A0, @ADDR_XZY_VEL
    ORI A0, A0, @ADDR_XZY_VEL
    //# load &player.vel as src_A
    OR A1, R0, A0
    //# load &windpower as src_B
    OR A2, R0, T1
    //# *A0 = *A1 + *A2
    JAL 0x80258C48
    NOP
    //# skip special entries
    ADD T1, T1, T6
    //# continue
    BEQ R0, R0, RETURN
    NOP
//#==========================================
EFFECT_LOOP_BREAK:
BEQ R0, R0, RETURN
NOP

RETURN:
//# restore regs
LW A3, 0x04(SP)
LW A2, 0x08(SP)
LW A1, 0x0C(SP)
LW A0, 0x10(SP)
LW RA, 0x14(SP)
J @HIJACK_RA
ADDIU SP, SP, 0x18
//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



//#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
.org 0x03F6B8D0 //# Data Area
//#==========================================