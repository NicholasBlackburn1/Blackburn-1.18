/***
 * this is the base class for the settings information uwu
 */
package space.nickyblackburn.settings;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonElement;

import org.apache.logging.log4j.core.util.StringEncoder;

import net.minecraft.network.chat.Component;
import space.nickyblackburn.utils.Consts;

public abstract class Settings {
    
    private final String name;
	private final String description;
	
	public Settings(String name, String description)
	{
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getDescription()
	{
		return description;
	}
	
	public abstract Component getComponent();
	
	public abstract void fromJson(JsonElement json);
	
	public abstract JsonElement toJson();
	
	public void update()
	{
		
	}
	
}