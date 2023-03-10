colors = [189 48 49;
    148 63 97; 
    92 25 29;
    22 126 134;
    58 142 140;
    86 44 62;
    20 180 133];

multiplier = [0.71 0.86 1 0.53];

start = 208;
for i=1:7
   for j=1:4
       z = floor(colors(i,:)*multiplier(j));
       fprintf("colors[%d] = new ImageColor(%d, %d, %d);\n", start, z(1), z(2), z(3));
       start = start + 1;
   end
end