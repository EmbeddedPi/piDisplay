package piDisplay;

public class fontTable {
	
	public static int convertChar (String fontChar){
		int fontCode = 0x55;
		// TODO Fill this little lot in
		switch (fontChar) {
		/* 
		case "?0":	fontCode = 0x00;
					break;
		case "?1":	fontCode = 0x01;
					break;
		case "?2":	fontCode = 0x02;
					break;
		case "?3":	fontCode = 0x03;
					break;
		case "?4":	fontCode = 0x04;
					break;
		case "?5":	fontCode = 0x05;
					break;
		case "?6":	fontCode = 0x06;
					break;
		case "?7":	fontCode = 0x07;
					break;	
		case "?8":	fontCode = 0x08;
					break;
		case "?9":	fontCode = 0x09;
					break;
		case "?A":	fontCode = 0x0A;
					break;
		case "?B":	fontCode = 0x0B;
					break;
		case "?C":	fontCode = 0x0C;
					break;
		case "?D":	fontCode = 0x0D;
					break;
		case "?E":	fontCode = 0x0E;
					break;
		case "?F":	fontCode = 0x0F;
					break;	
		*/
		case " ": 	fontCode = 0x20;
					break;
		case "!": 	fontCode = 0x21;
					break;
		case "\"": 	fontCode = 0x22;
					break;
		case "#": 	fontCode = 0x23;
					break;
		case "$": 	fontCode = 0x24;
					break;
		case "%": 	fontCode = 0x25;
					break;
		case "&": 	fontCode = 0x26;
					break;
		case "'": 	fontCode = 0x27;
					break;
		case "(": 	fontCode = 0x28;
					break;			
		case ")": 	fontCode = 0x29;
					break;
		case "*": 	fontCode = 0x2A;
					break;
		case "+": 	fontCode = 0x2B;
					break;
		case ",": 	fontCode = 0x2C;
					break;
		case "-": 	fontCode = 0x2D;
					break;
		case ".": 	fontCode = 0x2E;
					break;
		case "/": 	fontCode = 0x2F;
					break;
		case "0": 	fontCode = 0x30;
					break;
		case "1": 	fontCode = 0x31;
					break;
		case "2": 	fontCode = 0x32;
					break;
		case "3": 	fontCode = 0x33;
					break;
		case "4": 	fontCode = 0x34;
					break;
		case "5": 	fontCode = 0x35;
					break;
		case "6": 	fontCode = 0x36;
					break;
		case "7": 	fontCode = 0x37;
					break;
		case "8": 	fontCode = 0x38;
					break;			
		case "9": 	fontCode = 0x39;
					break;
		case ":": 	fontCode = 0x3A;
					break;
		case ";": 	fontCode = 0x3B;
					break;
		case "<": 	fontCode = 0x3C;
					break;
		case "=": 	fontCode = 0x3D;
					break;
		case ">": 	fontCode = 0x3E;
					break;
		case "?": 	fontCode = 0x3F;
					break;
		case "@": 	fontCode = 0x40;
					break;
		case "A": 	fontCode = 0x41;
					break;
		case "B": 	fontCode = 0x42;
					break;
		case "C": 	fontCode = 0x43;
					break;
		case "D": 	fontCode = 0x44;
					break;
		case "E": 	fontCode = 0x45;
					break;
		case "F": 	fontCode = 0x46;
					break;
		case "G": 	fontCode = 0x47;
					break;
		case "H": 	fontCode = 0x48;
					break;			
		case "I": 	fontCode = 0x49;
					break;
		case "J": 	fontCode = 0x4A;
					break;
		case "K": 	fontCode = 0x4B;
					break;
		case "L": 	fontCode = 0x4C;
					break;
		case "M": 	fontCode = 0x4D;
					break;
		case "N": 	fontCode = 0x4E;
					break;
		case "O": 	fontCode = 0x4F;
					break;
		case "P": 	fontCode = 0x50;
					break;
		case "Q": 	fontCode = 0x51;
					break;
		case "R": 	fontCode = 0x52;
					break;
		case "S": 	fontCode = 0x53;
					break;
		case "T": 	fontCode = 0x54;
					break;
		case "U": 	fontCode = 0x55;
					break;
		case "V": 	fontCode = 0x56;
					break;
		case "W": 	fontCode = 0x57;
					break;
		case "X": 	fontCode = 0x58;
					break;			
		case "Y": 	fontCode = 0x59;
					break;
		case "Z": 	fontCode = 0x5A;
					break;
		case "[": 	fontCode = 0x5B;
					break;
		case "Yen": fontCode = 0x5C;
					break;
		case "]": 	fontCode = 0x5D;
					break;
		case "^": 	fontCode = 0x5E;
					break;
		case "_": 	fontCode = 0x5F;
					break;
		case "`": 	fontCode = 0x60;
					break;
		case "a": 	fontCode = 0x61;
					break;
		case "b": 	fontCode = 0x62;
					break;
		case "c": 	fontCode = 0x63;
					break;
		case "d": 	fontCode = 0x64;
					break;
		case "e": 	fontCode = 0x65;
					break;
		case "f": 	fontCode = 0x66;
					break;
		case "g": 	fontCode = 0x67;
					break;
		case "h": 	fontCode = 0x68;
					break;			
		case "i": 	fontCode = 0x69;
					break;
		case "j": 	fontCode = 0x6A;
					break;
		case "k": 	fontCode = 0x6B;
					break;
		case "l": 	fontCode = 0x6C;
					break;
		case "m": 	fontCode = 0x6D;
					break;
		case "n": 	fontCode = 0x6E;
					break;
		case "o": 	fontCode = 0x6F;
					break;
		case "p": 	fontCode = 0x70;
					break;
		case "q": 	fontCode = 0x71;
					break;
		case "r": 	fontCode = 0x72;
					break;
		case "s": 	fontCode = 0x73;
					break;
		case "t": 	fontCode = 0x74;
					break;
		case "u": 	fontCode = 0x75;
					break;
		case "v": 	fontCode = 0x76;
					break;
		case "w": 	fontCode = 0x77;
					break;
		case "x": 	fontCode = 0x78;
					break;			
		case "y": 	fontCode = 0x79;
					break;
		case "z": 	fontCode = 0x7A;
					break;
		case "{": 	fontCode = 0x7B;
					break;
		case "|": 	fontCode = 0x7C;
					break;
		case "}": 	fontCode = 0x7D;
					break;	
		case "->": 	fontCode = 0x7E;
					break;					
		case "<-": 	fontCode = 0x7F;
					break;	
		/*
		 * Cases from 0xA0 to 0xFF once I know what's on my chips
		 * and more importantly if I can be bothered.
		 */
					
		default:	fontCode = 0x3F;
		}
		return fontCode;
	}
	
}