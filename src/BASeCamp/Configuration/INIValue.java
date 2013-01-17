package BASeCamp.Configuration;
public class INIValue {

	private String _Name="";
	private String _Value="";
	private String _Comment=""; 
	
	public String getName() {return _Name;}

	public void setName(String _Name) {	this._Name = _Name;}
	
	public String getValue() { return _Value;} 
	public void setValue(String value) { _Value=value;}
	
	public String getComment() { return _Comment;}
	public void setComment(String value){ _Comment=value;}
	
	/**
	 * finds first occurence of findthis in searchin, while ignoring quoted sections
	 * @param searchin
	 * @param findthis
	 * @return
	 */
	public static int FindFirstnonQuote(String searchin,String findthis){
		boolean inquote = false;
		try {
		for(int i=0;i<searchin.length()-findthis.length();i++){
			char grabchar = searchin.charAt(i);
			if(grabchar=='"') inquote=!inquote;
			
			if(!inquote && searchin.substring(i,i+findthis.length()).equals(findthis)){
				return i;
				
				
			}
			
			
			
			
		}
		return -1;
		}
		catch(StringIndexOutOfBoundsException exx){ return -1;}
		
	}
	public INIValue(String pName,String pValue){
		_Name = pName;
		_Value=pValue;
		
		
	}
	public INIValue(String ValueString){
		
		//parse an INI Value.
		//name=value ;comment
		
		//if there is a semicolon, strip off the comment and put it in _Comment.
		int commentstart=0;
		if((commentstart=ValueString.indexOf(";"))!=-1){
			//set comment...
			String commentstring = ValueString.substring(commentstart+1);
			//remove it from ValueString.
			_Comment=commentstring;
			ValueString=ValueString.substring(0, commentstart);
			
			
			
			
		}
		
		//now we need to search through the string to find the equals.
		int equalslocation = FindFirstnonQuote(ValueString,"=");
		//Name is from 0 to the location.
		//Value is from the location+1 to the end of the string.
		if(equalslocation==-1){
		_Name="!!COMMENT!!"; //sentinel to indicate it's a comment.
		_Value=null;
		}
		else {
		_Name = ValueString.substring(0,equalslocation);
		_Value = ValueString.substring(equalslocation+1);
		}
		
	}

	
	
	
	
}
