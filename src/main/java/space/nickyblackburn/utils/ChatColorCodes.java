package space.nickyblackburn.utils;

/**
 * this is for miencraft color codes
 */
public class ChatColorCodes {
    
   // this is the switch for minecraft colror codes
    public String chatColor(McColors color){
        String colorres = "";

        switch (color) {
            case BLACK:
            colorres = "§0";
            break;
    
            case RED:
            colorres = "4";
            break;
    
            case BLUE:
            colorres = "§9";
            break;
    
            case GREEN:
            colorres = "§a";
            break;
    
    
            case AQUA:
            colorres = "§b";
            break;
    
            case GOLD:
            colorres = "§6";
            break;
    
            case YELLOW:
            colorres = "§e";   
            break;
    
            case WHITE:
            colorres = "§f";
            break;
    
            case LIGHTPURPLE:
            colorres = "§d";
            break;
    
    
            case DARKGRAY:
            colorres = "§7";
            break;
    
    
            case DARKPURPLE:
            colorres = "§5";   
            break;
    
            case DARKRED:
            colorres = "§4";
            break;
    
            case DARKBLUE:
            colorres = "§1";
            break;
    
    
            case DARKGREEN:
            colorres = "§2";
            break;
    
            
            default:
            colorres = "§f";
                break;
        }
        return colorres;
    }
// this is for formatting codes
    public String formmatingCodes(FormatCodes codes){

        String formatcode = "";

        switch (codes) {
            case Obfuscated:
                formatcode = "§k";
                break;

                case Bold:
                formatcode = "§l";
                break;

                case Strikethrough:
                formatcode = "§m";
                break;

                case Underline:
                formatcode = "§n";
                break;

                case Italic:
                formatcode = "§o";
                break;

                case Reset:
                formatcode = "§r";
                break;

                
        
            default:
                break;
        }
        return formatcode;
    }


}
