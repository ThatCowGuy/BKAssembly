# BKAssembly
This repo hosts my custom assembly code that lets you build rectangular effect-boxes and place them in any Room. I made sure to code the assembly as dynamically as possible, so adding more effect-boxes does basically not increase the code-size at all (it only adds the ~24 Bytes that are needed to store the additional data). This means, it's no problem to add multiple boxes. The Jar-File is a GUI that makes it super easy to create new effect-boxes. Click [Add] after selecting an effect-type (which is only Wind for now) to create a new effect-Box. Then simply enter the rectangular bounds coordinates as integers, choose the Room you want it to be placed in, and enter the wind-strength per direction. If you want to remove an effect-box, [X] will do that. [Load From Data] is used to restore a set of effect-boxes from a past session, so you dont have to reenter everything over and over to make adjustments (That's also what the .data files are for). Finally, [Assemble] will parse your input into the .asm file and you are done. Now you only need to assemble & inject the .asm into the ROM (use CajeASM for this) and the effects are in. (Also make sure to place the "Basement Door" Object from MMM somewhere in the rooms, because my code unfortunaly depends on that object for now (am working on changing that).)

Currently implemented effects:
- Wind

Planned effects:
- Conveyor Belts
- Toxic Gas / Damage-over-Time

If the GUI doesnt work, you can also directly edit the .asm file I've added. It's kind of a hassle though, but here's what you have to do to manually add a box:
- remove every ".byte 0xFF" entry, aswell as the ".halfword 0x6666" one
- write 2 .byte entries that represent the Room-ID and the Data-Size (.byte = 1, .halfword = 2) of the effect Box
- write 6 .halfword entries that represent the rectangular bounds in Hex
- write 2 .byte entries that represent the effect-type (0x01 is Wind) and the size of the Special-Data (0x06 in the case of Wind)
- write 3 .halfword entries that represent the wind-strength in each direction xyz
- add back the ".halfword 0x6666" entry
- tally up the Data-Sizes of every box and add 2
- MAKE SURE that your tally is divisible by 8 by adding ".byte 0xFF" entrys until it is (otherwise it will crash because of instruction-misalignment)

Alternatively, you can compile the source code with ant, but try not to die from looking at my parser.
