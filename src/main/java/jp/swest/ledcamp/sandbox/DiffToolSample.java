package jp.swest.ledcamp.sandbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

public class DiffToolSample {
	static final String fileName1 = "gen/Project/Led.cpp";
	static final String fileName2 = "gen/ProjectTemp/Led.cpp";
	String file1;
	String file2;
	List<String> lines1;
	List<String> lines2;
	
	String[] functions = {"Led::ledOn", "Led::ledOff"};

	public static void main(String[] args) throws Exception{
		DiffToolSample sample = new DiffToolSample(fileName1, fileName2);
		sample.diff();
		Range range = sample.findFunctionRange("Led::ledOn");
		System.out.println(range.start+"-"+range.end);
	}
	
	public DiffToolSample(String file1, String file2) {
		try {
			lines1 = fileToLines(file1);
			lines2 = fileToLines(file2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void diff(){
		/* get and show Diff */
		Patch<String> diff = DiffUtils.diff(lines1, lines2);
		for(Delta<String> delta: diff.getDeltas()){
			System.out.println("type:"+delta.getType()+":  "+delta.getRevised().getLines().get(0));
		}
		
		/* patch sample */
		diff.getDeltas().remove(0);
		try {
			List<String> patchedSource = DiffUtils.patch(lines1, diff);
			for(String s : patchedSource){
//				System.out.println(s);
			}
		} catch (PatchFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Delta<String> d : diff.getDeltas()){
		}
	}
	
	private List<String> fileToLines(String file) throws IOException{
		List<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while((line=br.readLine())!=null){
			lines.add(line);
		}
		br.close();
		return lines;
	}
	
	private Range findFunctionRange(String funcName){
		Range range = new Range();
		int brancketCounter=0;
		int ln;
		String line;
		boolean inFunction = false;
		for(ln=0;ln<lines1.size();ln++){
			line = lines1.get(ln);
			if(inFunction){
				brancketCounter+=brancketCount(line);
				if(brancketCounter==0){
					range.end=ln;
					break;
				}
			}else if(line.contains(funcName)){
				range.start=ln;
				brancketCounter += brancketCount(line);
				inFunction = true;
			}
		}
		return range;
	}
	
	private int brancketCount(String str){
		int branckets = 0;
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			if(c=='{'){
				branckets++;
			}
			
			if(c=='}'){
				branckets--;
			}
		}
		return branckets;
	}

	class Range{
		int start;
		int end;
	}
}
