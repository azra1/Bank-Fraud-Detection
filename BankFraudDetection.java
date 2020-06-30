import java.util.*;
import java.io.*;

enum Month{
	Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;
}

class BankFraudDetection{

	public static void main(String args[])throws IOException{
		int i;
		FileInputStream transIn;
		FileInputStream customIn;
		
		try {
			transIn = new FileInputStream(args[1]);
			customIn = new FileInputStream(args[0]);
		} catch(FileNotFoundException e) {
			System.out.println("File Not Found");
			return;
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ShowFile File");
			return;
		}
		
		ArrayList <ArrayList<String>> fraudList =  new ArrayList<ArrayList<String>>(12); 
		for (i =0; i<12; i++) {
			fraudList.add(new ArrayList<String>());
		}
		
		Hashtable<String, Customer> customHash =  new Hashtable<String, Customer>(); 
		Hashtable<String,Integer> fraudAcc= new Hashtable<String, Integer>();
		
		int fraudKey=0;
		boolean isEnd= false;
		while(!isEnd) {
			
			// read account number
			String acc="";
			do {
				i= customIn.read();
				if (i!= '|' && i!=' ' )
					acc+= (char) i;
				
			}
			while(i!= '|');
			
			// read name
			String name="";
			do {
				i= customIn.read();
				if (i!= '|' && i!=' ' )
					name+= (char) i;
				
			}
			while(i!= '|');
			
			// read address
			String add="";
			do {
				i= customIn.read();
				if (i!= '|' && i!=' ' )
					add+= (char) i;
				
			}
			while(i!= '|');
			
			//read phone
			String phone="";
			do {
				i= customIn.read();
				if (i!=-1 && i!= '\n' && i!=' ' )
					phone+= (char) i;
				
			}
			while(i!=-1 && i!= '\n');
			
			customHash.put( acc,new Customer(acc, name, add, phone) );
			
			if (i==-1) {
				isEnd= true;
			}
			
			
		}
		
		//System.out.print("customHash="+customHash);
		
		isEnd= false;
		while(!isEnd) {
			
			// read transaction Id
			String id="";
			do {
				i= transIn.read();
				if (i!= '|' && i!=' ' )
					id+= (char) i;
				
			}
			while(i!= '|');
			
			//read date
			while(transIn.read()!='-')
				;
			
			String date="";
			do {
				i= transIn.read();
				if (i!= '-' && i!=' ' )
					date+= (char) i;
				
			}
			while(i!= '-');
			
			while(transIn.read()!='|')
				;
			//read from account number
			String from="";
			do {
				i= transIn.read();
				if (i!= '|' && i!=' ' )
					from+= (char) i;
				
			}
			while(i!= '|');
			
			//read to acc number
			String to="";
			do {
				i= transIn.read();
				if (i!= '|' && i!=' ' )
					to+= (char) i;
				
			}
			while(i!= '|');
			
			//read amount
			String amount="";
			do {
				i= transIn.read();
				if (i!= -1 && i!= '\n' && i!=' ' )
					amount+= (char) i;
				
			}
			while(i!= -1 && i!= '\n');
			
			
			
			try {
				Customer one=customHash.get(to);
				Customer two=customHash.get(from);
				//System.out.println(one.add+" "+one.phone);
				//System.out.println(two.add+" "+two.phone);
				//System.out.println("**");
				if ( one.isSame(two) ) {
					int month= Month.valueOf(date).ordinal();
					
					fraudList.get(month).add(id);
					
					if (fraudAcc.containsKey(from) && !fraudAcc.containsKey(to))
						fraudAcc.put(to,fraudAcc.get(from));
					else if (fraudAcc.containsKey(to) && !fraudAcc.containsKey(from))
						fraudAcc.put(from,fraudAcc.get(to));
					else if (!fraudAcc.containsKey(to) && !fraudAcc.containsKey(from)) {
						fraudAcc.put(from,fraudKey);
						fraudAcc.put(to,fraudKey);
						fraudKey+=1;
						
					}
					
				}
				
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				//System.out.println("Your Handling");
				e.printStackTrace();
			}
				
			
			if (i== -1) {
				isEnd = true;
			}
			
			
		}

		
		
		ArrayList<ArrayList<String>> accList= new ArrayList <ArrayList<String>>(fraudKey);
		for (i=0; i<fraudKey; i++)
			accList.add(new ArrayList<String>());
		fraudAcc.forEach((k, v) -> { 
			  
            accList.get(v).add(k);  
        } );  
		
		
		System.out.println("Expected Output:\n");
		Month monArr[]= Month.values();

		for (i=0; i<12; i++) {
			if(fraudList.get(i).size() >0) {
				System.out.println("For The Month of "+ monArr[i]);
				System.out.println("Suspicious Transactions :");
				for (String nextvalue: fraudList.get(i))
					System.out.println(nextvalue);
			}
			System.out.println();
		}
		
		System.out.println("Suspicious Accounts");
		for(ArrayList<String> list: accList) {
			if(list.size()>0) {
				System.out.println(list);
			}
		}
		
		transIn.close();
		customIn.close();
		
	}
		
}