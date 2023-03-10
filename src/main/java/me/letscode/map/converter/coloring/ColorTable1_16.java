package me.letscode.map.converter.coloring;

import java.awt.*;

public class ColorTable1_16 {

    public static final ImageColor[] colors = new ImageColor[236];

    public static ImageColor getMatchingColor(Color rgbColor) {
        if (rgbColor.getAlpha() == 0) {
            return colors[0];
        }

        double minDistance = 0.0;
        int colorIndex = 4;

        for (int i = 4; i < colors.length; i++) {
            ImageColor color = colors[i];

            int cRed = color.getRed();
            int cGreen = color.getGreen();
            int cBlue = color.getBlue();

            double cDistance = Math.sqrt(square(cRed - rgbColor.getRed()) + square(cGreen - rgbColor.getGreen()) + square(cBlue - rgbColor.getBlue()));

            if (i == 4) {
                minDistance = cDistance;
            } else if (cDistance < minDistance) {
                minDistance = cDistance;
                colorIndex = i;
            }
        }

        return colors[colorIndex];
    }


    private static int square(int i) {
        return i * i;
    }


    //double x = 0.4124564 * red + 0.3575761 * green + 0.3575761 * blue;
    //double y = 0.2126729 * red + 0.7151522 * green + 0.0721750 * blue;
    //double z = 0.0193339 * red + 0.1191920 * green + 0.9503041 * blue;


    static {
        ImageColor.counter = 0;
        colors[0] = new ImageColor(true);
        colors[1] = new ImageColor(true);
        colors[2] = new ImageColor(true);
        colors[3] = new ImageColor(true);
        colors[4] = new ImageColor(89, 125, 39);
        colors[5] = new ImageColor(109, 153, 48);
        colors[6] = new ImageColor(127, 178, 56);
        colors[7] = new ImageColor(67, 94, 29);
        colors[8] = new ImageColor(174, 164, 115);
        colors[9] = new ImageColor(213, 201, 140);
        colors[10] = new ImageColor(247, 233, 163);
        colors[11] = new ImageColor(130, 123, 86);
        colors[12] = new ImageColor(140, 140, 140);
        colors[13] = new ImageColor(171, 171, 171);
        colors[14] = new ImageColor(199, 199, 199);
        colors[15] = new ImageColor(105, 105, 105);
        colors[16] = new ImageColor(180, 0, 0);
        colors[17] = new ImageColor(220, 0, 0);
        colors[18] = new ImageColor(255, 0, 0);
        colors[19] = new ImageColor(135, 0, 0);
        colors[20] = new ImageColor(112, 112, 180);
        colors[21] = new ImageColor(138, 138, 220);
        colors[22] = new ImageColor(160, 160, 255);
        colors[23] = new ImageColor(84, 84, 135);
        colors[24] = new ImageColor(117, 117, 117);
        colors[25] = new ImageColor(144, 144, 144);
        colors[26] = new ImageColor(167, 167, 167);
        colors[27] = new ImageColor(88, 88, 88);
        colors[28] = new ImageColor(0, 87, 0);
        colors[29] = new ImageColor(0, 106, 0);
        colors[30] = new ImageColor(0, 124, 0);
        colors[31] = new ImageColor(0, 65, 0);
        colors[32] = new ImageColor(180, 180, 180);
        colors[33] = new ImageColor(220, 220, 220);
        colors[34] = new ImageColor(255, 255, 255);
        colors[35] = new ImageColor(135, 135, 135);
        colors[36] = new ImageColor(115, 118, 129);
        colors[37] = new ImageColor(141, 144, 158);
        colors[38] = new ImageColor(164, 168, 184);
        colors[39] = new ImageColor(86, 88, 97);
        colors[40] = new ImageColor(106, 76, 54);
        colors[41] = new ImageColor(130, 94, 66);
        colors[42] = new ImageColor(151, 109, 77);
        colors[43] = new ImageColor(79, 57, 40);
        colors[44] = new ImageColor(79, 79, 79);
        colors[45] = new ImageColor(96, 96, 96);
        colors[46] = new ImageColor(112, 112, 112);
        colors[47] = new ImageColor(59, 59, 59);
        colors[48] = new ImageColor(45, 45, 180);
        colors[49] = new ImageColor(55, 55, 220);
        colors[50] = new ImageColor(64, 64, 255);
        colors[51] = new ImageColor(33, 33, 135);
        colors[52] = new ImageColor(100, 84, 50);
        colors[53] = new ImageColor(123, 102, 62);
        colors[54] = new ImageColor(143, 119, 72);
        colors[55] = new ImageColor(75, 63, 38);
        colors[56] = new ImageColor(180, 177, 172);
        colors[57] = new ImageColor(220, 217, 211);
        colors[58] = new ImageColor(255, 252, 245);
        colors[59] = new ImageColor(135, 133, 129);
        colors[60] = new ImageColor(152, 89, 36);
        colors[61] = new ImageColor(186, 109, 44);
        colors[62] = new ImageColor(216, 127, 51);
        colors[63] = new ImageColor(114, 67, 27);
        colors[64] = new ImageColor(125, 53, 152);
        colors[65] = new ImageColor(153, 65, 186);
        colors[66] = new ImageColor(178, 76, 216);
        colors[67] = new ImageColor(94, 40, 114);
        colors[68] = new ImageColor(72, 108, 152);
        colors[69] = new ImageColor(88, 132, 186);
        colors[70] = new ImageColor(102, 153, 216);
        colors[71] = new ImageColor(54, 81, 114);
        colors[72] = new ImageColor(161, 161, 36);
        colors[73] = new ImageColor(197, 197, 44);
        colors[74] = new ImageColor(229, 229, 51);
        colors[75] = new ImageColor(121, 121, 27);
        colors[76] = new ImageColor(89, 144, 17);
        colors[77] = new ImageColor(109, 176, 21);
        colors[78] = new ImageColor(127, 204, 25);
        colors[79] = new ImageColor(67, 108, 13);
        colors[80] = new ImageColor(170, 89, 116);
        colors[81] = new ImageColor(208, 109, 142);
        colors[82] = new ImageColor(242, 127, 165);
        colors[83] = new ImageColor(128, 67, 87);
        colors[84] = new ImageColor(53, 53, 53);
        colors[85] = new ImageColor(65, 65, 65);
        colors[86] = new ImageColor(76, 76, 76);
        colors[87] = new ImageColor(40, 40, 40);
        colors[88] = new ImageColor(108, 108, 108);
        colors[89] = new ImageColor(132, 132, 132);
        colors[90] = new ImageColor(153, 153, 153);
        colors[91] = new ImageColor(81, 81, 81);
        colors[92] = new ImageColor(53, 89, 108);
        colors[93] = new ImageColor(65, 109, 132);
        colors[94] = new ImageColor(76, 127, 153);
        colors[95] = new ImageColor(40, 67, 81);
        colors[96] = new ImageColor(89, 44, 125);
        colors[97] = new ImageColor(109, 54, 153);
        colors[98] = new ImageColor(127, 63, 178);
        colors[99] = new ImageColor(67, 33, 94);
        colors[100] = new ImageColor(36, 53, 125);
        colors[101] = new ImageColor(44, 65, 153);
        colors[102] = new ImageColor(51, 76, 178);
        colors[103] = new ImageColor(27, 40, 94);
        colors[104] = new ImageColor(72, 53, 36);
        colors[105] = new ImageColor(88, 65, 44);
        colors[106] = new ImageColor(102, 76, 51);
        colors[107] = new ImageColor(54, 40, 27);
        colors[108] = new ImageColor(72, 89, 36);
        colors[109] = new ImageColor(88, 109, 44);
        colors[110] = new ImageColor(102, 127, 51);
        colors[111] = new ImageColor(54, 67, 27);
        colors[112] = new ImageColor(108, 36, 36);
        colors[113] = new ImageColor(132, 44, 44);
        colors[114] = new ImageColor(153, 51, 51);
        colors[115] = new ImageColor(81, 27, 27);
        colors[116] = new ImageColor(17, 17, 17);
        colors[117] = new ImageColor(21, 21, 21);
        colors[118] = new ImageColor(25, 25, 25);
        colors[119] = new ImageColor(13, 13, 13);
        colors[120] = new ImageColor(176, 168, 54);
        colors[121] = new ImageColor(215, 205, 66);
        colors[122] = new ImageColor(250, 238, 77);
        colors[123] = new ImageColor(132, 126, 40);
        colors[124] = new ImageColor(64, 154, 150);
        colors[125] = new ImageColor(79, 188, 183);
        colors[126] = new ImageColor(92, 219, 213);
        colors[127] = new ImageColor(48, 115, 112);
        colors[128] = new ImageColor(52, 90, 180);
        colors[129] = new ImageColor(63, 110, 220);
        colors[130] = new ImageColor(74, 128, 255);
        colors[131] = new ImageColor(39, 67, 135);
        colors[132] = new ImageColor(0, 153, 40);
        colors[133] = new ImageColor(0, 187, 50);
        colors[134] = new ImageColor(0, 217, 58);
        colors[135] = new ImageColor(0, 114, 30);
        colors[136] = new ImageColor(91, 60, 34);
        colors[137] = new ImageColor(111, 74, 42);
        colors[138] = new ImageColor(129, 86, 49);
        colors[139] = new ImageColor(68, 45, 25);
        colors[140] = new ImageColor(79, 1, 0);
        colors[141] = new ImageColor(96, 1, 0);
        colors[142] = new ImageColor(112, 2, 0);
        colors[143] = new ImageColor(59, 1, 0);
        colors[144] = new ImageColor(147, 124, 113);
        colors[145] = new ImageColor(180, 152, 138);
        colors[146] = new ImageColor(209, 177, 161);
        colors[147] = new ImageColor(110, 93, 85);
        colors[148] = new ImageColor(112, 57, 25);
        colors[149] = new ImageColor(137, 70, 31);
        colors[150] = new ImageColor(159, 82, 36);
        colors[151] = new ImageColor(84, 43, 19);
        colors[152] = new ImageColor(105, 61, 76);
        colors[153] = new ImageColor(128, 75, 93);
        colors[154] = new ImageColor(149, 87, 108);
        colors[155] = new ImageColor(78, 46, 57);
        colors[156] = new ImageColor(79, 76, 97);
        colors[157] = new ImageColor(96, 93, 119);
        colors[158] = new ImageColor(112, 108, 138);
        colors[159] = new ImageColor(59, 57, 73);
        colors[160] = new ImageColor(131, 93, 25);
        colors[161] = new ImageColor(160, 114, 31);
        colors[162] = new ImageColor(186, 133, 36);
        colors[163] = new ImageColor(98, 70, 19);
        colors[164] = new ImageColor(72, 82, 37);
        colors[165] = new ImageColor(88, 100, 45);
        colors[166] = new ImageColor(103, 117, 53);
        colors[167] = new ImageColor(54, 61, 28);
        colors[168] = new ImageColor(112, 54, 55);
        colors[169] = new ImageColor(138, 66, 67);
        colors[170] = new ImageColor(160, 77, 78);
        colors[171] = new ImageColor(84, 40, 41);
        colors[172] = new ImageColor(40, 28, 24);
        colors[173] = new ImageColor(49, 35, 30);
        colors[174] = new ImageColor(57, 41, 35);
        colors[175] = new ImageColor(30, 21, 18);
        colors[176] = new ImageColor(95, 75, 69);
        colors[177] = new ImageColor(116, 92, 84);
        colors[178] = new ImageColor(135, 107, 98);
        colors[179] = new ImageColor(71, 56, 51);
        colors[180] = new ImageColor(61, 64, 64);
        colors[181] = new ImageColor(75, 79, 79);
        colors[182] = new ImageColor(87, 92, 92);
        colors[183] = new ImageColor(46, 48, 48);
        colors[184] = new ImageColor(86, 51, 62);
        colors[185] = new ImageColor(105, 62, 75);
        colors[186] = new ImageColor(122, 73, 88);
        colors[187] = new ImageColor(64, 38, 46);
        colors[188] = new ImageColor(53, 43, 64);
        colors[189] = new ImageColor(65, 53, 79);
        colors[190] = new ImageColor(76, 62, 92);
        colors[191] = new ImageColor(40, 32, 48);
        colors[192] = new ImageColor(53, 35, 24);
        colors[193] = new ImageColor(65, 43, 30);
        colors[194] = new ImageColor(76, 50, 35);
        colors[195] = new ImageColor(40, 26, 18);
        colors[196] = new ImageColor(53, 57, 29);
        colors[197] = new ImageColor(65, 70, 36);
        colors[198] = new ImageColor(76, 82, 42);
        colors[199] = new ImageColor(40, 43, 22);
        colors[200] = new ImageColor(100, 42, 32);
        colors[201] = new ImageColor(122, 51, 39);
        colors[202] = new ImageColor(142, 60, 46);
        colors[203] = new ImageColor(75, 31, 24);
        colors[204] = new ImageColor(26, 15, 11);
        colors[205] = new ImageColor(31, 18, 13);
        colors[206] = new ImageColor(37, 22, 16);
        colors[207] = new ImageColor(19, 11, 8);
        // 1.16+ colors
        colors[208] = new ImageColor(134, 34, 34);
        colors[209] = new ImageColor(162, 41, 42);
        colors[210] = new ImageColor(189, 48, 49);
        colors[211] = new ImageColor(100, 25, 25);
        colors[212] = new ImageColor(105, 44, 68);
        colors[213] = new ImageColor(127, 54, 83);
        colors[214] = new ImageColor(148, 63, 97);
        colors[215] = new ImageColor(78, 33, 51);
        colors[216] = new ImageColor(65, 17, 20);
        colors[217] = new ImageColor(79, 21, 24);
        colors[218] = new ImageColor(92, 25, 29);
        colors[219] = new ImageColor(48, 13, 15);
        colors[220] = new ImageColor(15, 89, 95);
        colors[221] = new ImageColor(18, 108, 115);
        colors[222] = new ImageColor(22, 126, 134);
        colors[223] = new ImageColor(11, 66, 71);
        colors[224] = new ImageColor(41, 100, 99);
        colors[225] = new ImageColor(49, 122, 120);
        colors[226] = new ImageColor(58, 142, 140);
        colors[227] = new ImageColor(30, 75, 74);
        colors[228] = new ImageColor(61, 31, 44);
        colors[229] = new ImageColor(73, 37, 53);
        colors[230] = new ImageColor(86, 44, 62);
        colors[231] = new ImageColor(45, 23, 32);
        colors[232] = new ImageColor(14, 127, 94);
        colors[233] = new ImageColor(17, 154, 114);
        colors[234] = new ImageColor(20, 180, 133);
        colors[235] = new ImageColor(10, 95, 70);
    }

}
