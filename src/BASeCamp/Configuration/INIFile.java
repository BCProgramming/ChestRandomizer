package BASeCamp.Configuration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class INIFile {

	private static final String newLine = System.getProperty("line.separator");
	private Map<String,INISection> _Sections = new LinkedHashMap<String,INISection>() ;
	
	
	public INIFile(){
		
		
	}
	public INIFile(FileReader fr) throws IOException{
		load(new Scanner(fr));
			
	}
	public INIFile(Scanner sc) throws IOException
	{
		if(sc==null) return;
		load(sc);
		
	}
	private void load(Scanner br) throws IOException{
		
		
		String CurrentName="global";
		String lineread=null;
		INISection CurrentSection = new INISection("global",null);
		_Sections.put("global", CurrentSection);
		while(br.hasNextLine() && null!=(lineread=br.nextLine())){
			lineread=lineread.trim();
			//System.out.println("INI:" + lineread);
			if(!lineread.startsWith(";")){
				if(lineread.startsWith("[")){
					
					//section name.
					String usecomment=null;
					int sectionnameend = lineread.indexOf(']');
					CurrentName = lineread.substring(1, sectionnameend);
					int commentstart;
					if((commentstart=lineread.indexOf(';',sectionnameend)) !=-1)
						usecomment = lineread.substring(commentstart+1);
					CurrentSection = new INISection(CurrentName,usecomment);
					_Sections.put(CurrentSection.getName(), CurrentSection);
					
					
				}
				else{
					
					CurrentSection.addValue(new INIValue(lineread));
					
				}
				
				
				
			}
			
			
			
		}
		br.close();
		
		
		
		
		
		
		
	}
	public INIFile(String sFileName) throws IOException{
		
		File sTarget = new File(sFileName);
		if(sTarget.exists()){
			load(new Scanner(sTarget));
			
		}
		
		
	}
	public boolean hasSection(String testname){
		
		return _Sections.containsKey(testname);
		
		
	}
	public INISection getSection(String sectionName){
		if(!_Sections.containsKey(sectionName)){
			_Sections.put(sectionName, new INISection(sectionName,null));
			
			
		}
		return _Sections.get(sectionName);
		
		
	}
	public INIValue getValue(String sectionName,String valueName,String Default){
		return getSection(sectionName).getValue(valueName,Default);
		
	}
	public void Save(String FileName) throws IOException{
		Save(new FileWriter(FileName));
	}
	public void Save(FileWriter fw) throws IOException{
		Save(new BufferedWriter(fw));
	}
	public void Save(BufferedWriter bw) throws IOException{
		{
			for(INISection iterate:_Sections.values()){
				bw.write("[" + iterate.getName() + "]" );
				if(iterate.getComment()!=null && iterate.getComment().length() >0) bw.write(" ;" + iterate.getComment());
				bw.write(newLine);
				for(INIValue iteratevalue: iterate.getValues()){
					//write this value.
					if(!iteratevalue.getName().equals("!!COMMENT!!")){
					bw.write(iteratevalue.getName());
					bw.write("=" + iteratevalue.getValue());
					}
					if(iteratevalue.getComment().length() > 0){
						bw.write(";" + iteratevalue.getComment());
						
					}
					bw.write(newLine);
				}
				
				
				
			}
			
			bw.close();			
			
		}
		
		
	}
	
}
