package space.nickyblackburn.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import space.nickyblackburn.utils.Consts;

public class BlackburnCommand {
    

   public int performCommand(CommandDispatcher<CommandSourceStack> dispatcher,CommandSourceStack p_82118_, String p_82119_) {
    StringReader stringreader = new StringReader(p_82119_);
    if (stringreader.canRead() && stringreader.peek() == '/') {
       stringreader.skip();
    }

    p_82118_.getServer().getProfiler().push(p_82119_);

    try {
       try {
          return dispatcher.execute(stringreader, p_82118_);
       } catch (CommandRuntimeException commandruntimeexception) {
          p_82118_.sendFailure(commandruntimeexception.getComponent());
          return 0;
       } catch (CommandSyntaxException commandsyntaxexception) {
          p_82118_.sendFailure(ComponentUtils.fromMessage(commandsyntaxexception.getRawMessage()));
          if (commandsyntaxexception.getInput() != null && commandsyntaxexception.getCursor() >= 0) {
             int j = Math.min(commandsyntaxexception.getInput().length(), commandsyntaxexception.getCursor());
             MutableComponent mutablecomponent1 = (new TextComponent("")).withStyle(ChatFormatting.GRAY).withStyle((p_82134_) -> {
                return p_82134_.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, p_82119_));
             });
             if (j > 10) {
                mutablecomponent1.append("...");
             }

             mutablecomponent1.append(commandsyntaxexception.getInput().substring(Math.max(0, j - 10), j));
             if (j < commandsyntaxexception.getInput().length()) {
                Component component = (new TextComponent(commandsyntaxexception.getInput().substring(j))).withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.UNDERLINE});
                mutablecomponent1.append(component);
             }

             mutablecomponent1.append((new TranslatableComponent("command.context.here")).withStyle(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.ITALIC}));
             p_82118_.sendFailure(mutablecomponent1);
          }
       } catch (Exception exception) {
          MutableComponent mutablecomponent = new TextComponent(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
          
             Consts.log("Command exception: {}"+p_82119_+exception);
             StackTraceElement[] astacktraceelement = exception.getStackTrace();

             for(int i = 0; i < Math.min(astacktraceelement.length, 3); ++i) {
                mutablecomponent.append("\n\n").append(astacktraceelement[i].getMethodName()).append("\n ").append(astacktraceelement[i].getFileName()).append(":").append(String.valueOf(astacktraceelement[i].getLineNumber()));
             }
          
          p_82118_.sendFailure((new TranslatableComponent("command.failed")).withStyle((p_82137_) -> {
             return p_82137_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, mutablecomponent));
          }));
         

          return 0;
       }

       return 0;
    } finally {
       p_82118_.getServer().getProfiler().pop();
    }
    
 }

}
