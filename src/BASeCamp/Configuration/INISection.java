package BASeCamp.Configuration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class INISection {

	private Map<String,INIValue> _Values = new TreeMap<String,INIValue>(String.CASE_INSENSITIVE_ORDER);
	
	private String _Name;
	private String _Comment;
	
	public String getName() { return _Name;}
	public void setName(String value) { _Name=value;}
	public String getComment() { return _Comment;}
	public void setComment(String value){_Comment=value;}
	public INIValue getValue(String ValueName){
		return getValue(ValueName,"");
		
	}
	public INIValue getValue(String ValueName,String DefaultValue){
		if(!_Values.containsKey(ValueName)){
			
			_Values.put(ValueName,new INIValue(ValueName,DefaultValue));
			
			
		}
		return _Values.get(ValueName);
		
		
	}
	public void addValue(INIValue added){
		
		_Values.put(added.getName(),added);
		
		
	}
	public Iterable<INIValue> getValues(){
		
		return _Values.values();
		
		
	}
	public INIValue setValue(String ValueName,String newValue){
		
		if(!_Values.containsKey(ValueName)){
			INIValue createdvalue = new INIValue(ValueName,newValue);
			_Values.put(ValueName,createdvalue);
			return createdvalue;
			
		}
		
		_Values.get(ValueName).setValue(newValue);
		return _Values.get(ValueName);
		
		
		
		
		
	}
	public INISection(String Name,String Comment){
		
		_Name=Name;
		_Comment = Comment;
		
	}
	
	
	
	
	
	
	
	
}
