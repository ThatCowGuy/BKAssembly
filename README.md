# BKAssembly
This repo hosts my custom assembly code that lets you build rectangular effect-boxes and place them in any Room. I made sure to code the assembly as dynamically as possible, so adding more effect-boxes only increases the code-size a bit (it's 40 Bytes per windbox). This means it's no problem to add multiple boxes. The Jar-File is a GUI that makes it super easy to create new effect-boxes. Click [Add] after selecting an effect-type (which is only Wind for now) to create a new effect-Box. Then simply enter the rectangular bounds coordinates as floats, choose the Room you want it to be placed in, and enter the wind-strength per direction. If you want to remove an effect-box, [X] will do that. [Load From Data] is used to restore a set of effect-boxes from a past session, so you dont have to reenter everything over and over to make adjustments (That's also what the .data files are for). Finally, [Assemble] will parse your input into the .asm file and you are done. Now you only need to assemble & inject the .asm into the ROM (use CajeASM for this) and the effects are in. (Also make sure to place the "Basement Door" Object from MMM in the first room of your Hack, like FileSelect or the IntroCutscenes, because the hijack method is called by that Object at runtime.)

Currently implemented effects:
- Wind

Planned effects:
- Conveyor Belts
- Toxic Gas / Damage-over-Time

If the GUI doesnt work, you can also directly edit the .asm file I've added. It's kind of a hassle though, but here's what you have to do to manually add a box (only change the Data-Section at the bottom):
- remove the ".halfword 0x6666" entry
- write 2 .byte entries that represent the Room-ID and the Data-Size (0x28 for Wind) of the effect Box
- write 2 .byte entries that represent the effect-type (0x01 for Wind) and the size of the Special-Data (0x0C for Wind)
- write 6 .word entries that represent the rectangular bounds in Hex (https://www.h-schmidt.net/FloatConverter/IEEE754.html)
- write 3 .halfword entries that represent the wind-strength in each direction xyz in Hex (https://www.h-schmidt.net/FloatConverter/IEEE754.html)
- add back the ".halfword 0x6666" entry

Alternatively, you can compile the source code with ant, but try not to die from looking at my code.
