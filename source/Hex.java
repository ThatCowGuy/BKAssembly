public class Hex
{
    public int digits;
    public long value;
    public long complement;
    public String hex;
    public static char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',  'E', 'F'};

    public Hex(long val)
    {
        int digits = 1;
        while (val >= 16)
        {
            digits++;
            val /= 16;
        }
        this.digits = digits;
        if (val < 0)
        {
            val = (int)Math.pow(16, digits) + val;
        }
        this.value = val;
        this.calcComplement();
        this.hex = Dec2Hex(digits);
    }
    public Hex(long val, int digits)
    {
        this.digits = digits;
        if (val < 0)
        {
            val = (int)Math.pow(16, digits) + val;
        }
        this.value = val;
        this.calcComplement();
        this.hex = Dec2Hex(digits);
    }
    public Hex(float val, int digits)
    {
        this.value = Float.floatToIntBits(val);
        this.digits = digits;
        if (this.value < 0)
        {
            this.value = (int)Math.pow(16, digits) + this.value;
        }
        this.calcComplement();
        this.hex = Dec2Hex(digits);
    }
    public Hex(String s)
    {
        // get rid of whitespaces and special shit
        s = s.replaceAll(" ","");

        if (s.contains(".") == true)
        {
            float fval = new Float(s);
            this.digits = 8;
            this.value = Float.floatToIntBits(fval);
            if (this.value < 0)
            {
                this.value = (int)Math.pow(16, digits) + this.value;
            }
        }
        else
        {
            if (s.contains("0x") == true)
                s = s.substring(2);
            this.digits = s.length();
            this.value = Hex.Hex2Dec(s);
        }
        this.calcComplement();
        this.hex = Dec2Hex(8);
    }
    public Hex(String s, int digits, boolean isfloat)
    {
        // get rid of whitespaces and special shit
        s = s.replaceAll(" ","");

        if (isfloat == true)
        {
            float fval = new Float(s);
            this.value = Float.floatToIntBits(fval);
        }
        else
        {
            if (s.contains("0x") == true)
                s = s.substring(2);
            while (s.length() < digits)
                s += "0";
            this.value = Hex.Hex2Dec(s);
        }
        this.digits = digits;
        this.calcComplement();
        this.hex = Dec2Hex(digits);
    }

    @Override
    public String toString()
    {
        return this.hex;
    }
    public String printFloat()
    {
        return ""+Float.intBitsToFloat((int)this.value);
    }

    // calc the 2's complement
    void calcComplement()
    {   
        // check if this is a negative number
        if ( this.value > 0.5*Math.pow(16, this.digits) )
        {
            this.complement = this.value - (int)Math.pow(16, this.digits);
        }
        else
        {
            this.complement = this.value;
        }
        return;
    }
    static long calcComplement(long value, long digits)
    {   
        // check if this is a negative number
        if ( value > 0.5*Math.pow(16, digits) )
        {
            return value - (int)Math.pow(16, digits);
        }
        else
        {
            return value;
        }
    }

    static public Hex concat(Hex h1, Hex h2)
    {
        return new Hex(h1.value*(long)Math.pow(16, 2)+h2.value, h1.digits+h2.digits);
    }

    public byte[] outputInBytes()
    {
        byte[] array = new byte[(int)this.digits/2];
        int counter = 0;

        for (long d = this.digits; d > 0; d -= 2)
        {
            String hexByte = "" + this.hex.charAt(counter*2+0) + this.hex.charAt(counter*2+1);
            array[counter] = (byte)Hex2Dec(hexByte);
            counter++;
        }
        return array;
    }

    public Hex[] split()
    {
        Hex[] array = new Hex[this.digits/2];
        int counter = 0;

        for (long d = this.digits; d > 0; d -= 2)
        {
            String hexByte = "" + this.hex.charAt(counter*2+0) + this.hex.charAt(counter*2+1);
            array[counter] = new Hex(Hex2Dec(hexByte), 2);
            counter++;
        }
        return array;
    }

    public boolean compare(Hex h)
    {
        if (this.value != h.value)
            return false;
        
        return true;
    }

    String Dec2Hex()
    {
        return Integer.toHexString((int)value).toUpperCase();
    }
    String Dec2Hex(long digits)
    {
        String res = Integer.toHexString((int)value).toUpperCase();
        while (res.length() < digits)
        {
            res = "0" + res;
        }
        return res;
    }
    static String Dec2Hex(long value, long digits)
    {
        String res = Integer.toHexString((int)value).toUpperCase();
        while (res.length() < digits)
        {
            res = "0" + res;
        }
        return res;
    }

    long Hex2Dec()
    {
        long temp_val = 0;
        for (int i = 0; i < this.hex.length(); i++)
        {
            temp_val += HexChar2Dec(this.hex.charAt(i)) * (int)Math.pow(16, (this.hex.length()-1-i));
        }
        return temp_val;
    }
    static long Hex2Dec(String s)
    {
        long temp_val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            temp_val += HexChar2Dec(s.charAt(i)) * (int)Math.pow(16, (s.length()-1-i));
        }
        return temp_val;
    }
    static long HexChar2Dec(char c)
    {
        switch(c)
        {
            case('0'): return 0;
            case('1'): return 1;
            case('2'): return 2;
            case('3'): return 3;
            case('4'): return 4;
            case('5'): return 5;
            case('6'): return 6;
            case('7'): return 7;
            case('8'): return 8;
            case('9'): return 9;
            case('a'):
            case('A'): return 10;
            case('b'):
            case('B'): return 11;
            case('c'):
            case('C'): return 12;
            case('d'):
            case('D'): return 13;
            case('e'):
            case('E'): return 14;
            case('f'):
            case('F'): return 15;
        }
        return -1;
    }

}