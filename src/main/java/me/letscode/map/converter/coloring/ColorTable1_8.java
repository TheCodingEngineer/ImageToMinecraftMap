package me.letscode.map.converter.coloring;

import java.awt.Color;

public class ColorTable1_8 {

    public static final ImageColor[] colors = new ImageColor[144];

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
        colors[0] = new ImageColor(0, true);
        colors[1] = new ImageColor(1, true);
        colors[2] = new ImageColor(2, true);
        colors[3] = new ImageColor(3, true);
        colors[4] = new ImageColor(4, 88, 124, 39);
        colors[5] = new ImageColor(5, 108, 151, 47);
        colors[6] = new ImageColor(6, 125, 176, 55);
        colors[7] = new ImageColor(7, 66, 93, 29);
        colors[8] = new ImageColor(8, 172, 162, 114);
        colors[9] = new ImageColor(9, 210, 199, 138);
        colors[10] = new ImageColor(10, 244, 230, 161);
        colors[11] = new ImageColor(11, 128, 122, 85);
        colors[12] = new ImageColor(12, 138, 138, 138);
        colors[13] = new ImageColor(13, 169, 169, 169);
        colors[14] = new ImageColor(14, 197, 197, 197);
        colors[15] = new ImageColor(15, 104, 104, 104);
        colors[16] = new ImageColor(16, 178, 0, 0);
        colors[17] = new ImageColor(17, 217, 0, 0);
        colors[18] = new ImageColor(18, 252, 0, 0);
        colors[19] = new ImageColor(19, 133, 0, 0);
        colors[20] = new ImageColor(20, 111, 111, 178);
        colors[21] = new ImageColor(21, 136, 136, 217);
        colors[22] = new ImageColor(22, 158, 158, 252);
        colors[23] = new ImageColor(23, 83, 83, 133);
        colors[24] = new ImageColor(24, 116, 116, 116);
        colors[25] = new ImageColor(25, 142, 142, 142);
        colors[26] = new ImageColor(26, 165, 165, 165);
        colors[27] = new ImageColor(27, 87, 87, 87);
        colors[28] = new ImageColor(28, 0, 86, 0);
        colors[29] = new ImageColor(29, 0, 105, 0);
        colors[30] = new ImageColor(30, 0, 123, 0);
        colors[31] = new ImageColor(31, 0, 64, 0);
        colors[32] = new ImageColor(32, 178, 178, 178);
        colors[33] = new ImageColor(33, 217, 217, 217);
        colors[34] = new ImageColor(34, 252, 252, 252);
        colors[35] = new ImageColor(35, 133, 133, 133);
        colors[36] = new ImageColor(36, 114, 117, 127);
        colors[37] = new ImageColor(37, 139, 142, 156);
        colors[38] = new ImageColor(38, 162, 166, 182);
        colors[39] = new ImageColor(39, 85, 87, 96);
        colors[40] = new ImageColor(40, 105, 75, 53);
        colors[41] = new ImageColor(41, 128, 93, 65);
        colors[42] = new ImageColor(42, 149, 108, 76);
        colors[43] = new ImageColor(43, 78, 56, 39);
        colors[44] = new ImageColor(44, 78, 78, 78);
        colors[45] = new ImageColor(45, 95, 95, 95);
        colors[46] = new ImageColor(46, 111, 111, 111);
        colors[47] = new ImageColor(47, 58, 58, 58);
        colors[48] = new ImageColor(48, 44, 44, 178);
        colors[49] = new ImageColor(49, 54, 54, 217);
        colors[50] = new ImageColor(50, 63, 63, 252);
        colors[51] = new ImageColor(51, 33, 33, 133);
        colors[52] = new ImageColor(52, 99, 83, 49);
        colors[53] = new ImageColor(53, 122, 101, 61);
        colors[54] = new ImageColor(54, 141, 118, 71);
        colors[55] = new ImageColor(55, 74, 62, 38);
        colors[56] = new ImageColor(56, 178, 175, 170);
        colors[57] = new ImageColor(57, 217, 214, 208);
        colors[58] = new ImageColor(58, 252, 249, 242);
        colors[59] = new ImageColor(59, 133, 131, 127);
        colors[60] = new ImageColor(60, 150, 88, 36);
        colors[61] = new ImageColor(61, 184, 108, 43);
        colors[62] = new ImageColor(62, 213, 125, 50);
        colors[63] = new ImageColor(63, 113, 66, 27);
        colors[64] = new ImageColor(64, 124, 52, 150);
        colors[65] = new ImageColor(65, 151, 64, 184);
        colors[66] = new ImageColor(66, 176, 75, 213);
        colors[67] = new ImageColor(67, 93, 39, 113);
        colors[68] = new ImageColor(68, 71, 107, 150);
        colors[69] = new ImageColor(69, 87, 130, 184);
        colors[70] = new ImageColor(70, 101, 151, 213);
        colors[71] = new ImageColor(71, 53, 80, 113);
        colors[72] = new ImageColor(72, 159, 159, 36);
        colors[73] = new ImageColor(73, 195, 195, 43);
        colors[74] = new ImageColor(74, 226, 226, 50);
        colors[75] = new ImageColor(75, 120, 120, 27);
        colors[76] = new ImageColor(76, 88, 142, 17);
        colors[77] = new ImageColor(77, 108, 174, 21);
        colors[78] = new ImageColor(78, 125, 202, 25);
        colors[79] = new ImageColor(79, 66, 107, 13);
        colors[80] = new ImageColor(80, 168, 88, 115);
        colors[81] = new ImageColor(81, 206, 108, 140);
        colors[82] = new ImageColor(82, 239, 125, 163);
        colors[83] = new ImageColor(83, 126, 66, 86);
        colors[84] = new ImageColor(84, 52, 52, 52);
        colors[85] = new ImageColor(85, 64, 64, 64);
        colors[86] = new ImageColor(86, 75, 75, 75);
        colors[87] = new ImageColor(87, 39, 39, 39);
        colors[88] = new ImageColor(88, 107, 107, 107);
        colors[89] = new ImageColor(89, 130, 130, 130);
        colors[90] = new ImageColor(90, 151, 151, 151);
        colors[91] = new ImageColor(91, 80, 80, 80);
        colors[92] = new ImageColor(92, 52, 88, 107);
        colors[93] = new ImageColor(93, 64, 108, 130);
        colors[94] = new ImageColor(94, 75, 125, 151);
        colors[95] = new ImageColor(95, 39, 66, 80);
        colors[96] = new ImageColor(96, 88, 43, 124);
        colors[97] = new ImageColor(97, 108, 53, 151);
        colors[98] = new ImageColor(98, 125, 62, 176);
        colors[99] = new ImageColor(99, 66, 33, 93);
        colors[100] = new ImageColor(100, 36, 52, 124);
        colors[101] = new ImageColor(101, 43, 64, 151);
        colors[102] = new ImageColor(102, 50, 75, 176);
        colors[103] = new ImageColor(103, 27, 39, 93);
        colors[104] = new ImageColor(104, 71, 52, 36);
        colors[105] = new ImageColor(105, 87, 64, 43);
        colors[106] = new ImageColor(106, 101, 75, 50);
        colors[107] = new ImageColor(107, 53, 39, 27);
        colors[108] = new ImageColor(108, 71, 88, 36);
        colors[109] = new ImageColor(109, 87, 108, 43);
        colors[110] = new ImageColor(110, 101, 125, 50);
        colors[111] = new ImageColor(111, 53, 66, 27);
        colors[112] = new ImageColor(112, 107, 36, 36);
        colors[113] = new ImageColor(113, 130, 43, 43);
        colors[114] = new ImageColor(114, 151, 50, 50);
        colors[115] = new ImageColor(115, 80, 27, 27);
        colors[116] = new ImageColor(116, 17, 17, 17);
        colors[117] = new ImageColor(117, 21, 21, 21);
        colors[118] = new ImageColor(118, 25, 25, 25);
        colors[119] = new ImageColor(119, 13, 13, 13);
        colors[120] = new ImageColor(120, 174, 166, 53);
        colors[121] = new ImageColor(121, 212, 203, 65);
        colors[122] = new ImageColor(122, 247, 235, 76);
        colors[123] = new ImageColor(123, 130, 125, 39);
        colors[124] = new ImageColor(124, 63, 152, 148);
        colors[125] = new ImageColor(125, 78, 186, 181);
        colors[126] = new ImageColor(126, 91, 216, 210);
        colors[127] = new ImageColor(127, 47, 114, 111);
        colors[128] = new ImageColor(128, 51, 89, 178);
        colors[129] = new ImageColor(129, 62, 109, 217);
        colors[130] = new ImageColor(130, 73, 129, 252);
        colors[131] = new ImageColor(131, 39, 66, 133);
        colors[132] = new ImageColor(132, 0, 151, 39);
        colors[133] = new ImageColor(133, 0, 185, 49);
        colors[134] = new ImageColor(134, 0, 214, 57);
        colors[135] = new ImageColor(135, 0, 113, 30);
        colors[136] = new ImageColor(136, 90, 59, 34);
        colors[137] = new ImageColor(137, 110, 73, 41);
        colors[138] = new ImageColor(138, 127, 85, 48);
        colors[139] = new ImageColor(139, 67, 44, 25);
        colors[140] = new ImageColor(140, 78, 1, 0);
        colors[141] = new ImageColor(141, 95, 1, 0);
        colors[142] = new ImageColor(142, 111, 2, 0);
        colors[143] = new ImageColor(143, 58, 1, 0);

    }

}
