package StudyInWinter;

import java.awt.Color;
import java.util.Scanner;

import javax.swing.JFrame;

public class Winter_Study {

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		/*Scanner scan = new Scanner(System.in);
		System.out.print("������a1:");
		int a1 = scan.nextInt();
		
		System.out.print("������a2:");
		int a2 = scan.nextInt();
		
		System.out.print("������a3:");
		int a3 = scan.nextInt();

		System.out.print("������b1:");
		int b1 = scan.nextInt();

		System.out.print("������b2:");
		int b2 = scan.nextInt();

		System.out.print("������b3:");
		int b3 = scan.nextInt();

		System.out.print("������c1:");
		int c1 = scan.nextInt();

		System.out.print("������c2:");
		int c2 = scan.nextInt();

		System.out.print("������c3:");
		int c3 = scan.nextInt();
		System.out.println();
		
		int result = a1*b2*c3-a1*b3*c2-a2*b1*c3+a2*b3*c1+a3*b1*c2-a3*b2*c1;
		System.out.println("���Ϊ:"+result);
		
		scan.close();*/
		
		/*String board[][]=new String [41][41];
		for(int x=0;x<=40;x++) {
			for(int y=0;y<=40;y++) {
				if((x-20)*(x-20)+(y-20)*(y-20)<=200)
					board[x][y]= "**";
				else board[x][y]="  ";
			}
		}
		
		for(int x=0;x<=40;x++) {
			
			for(int y=0;y<=40;y++) {
				
				System.out.print(board[x][y]);
			}
			System.out.println();
		}*/	
		
		
		JFrame window=new EventDemo();
		window.setTitle("EventDemo");
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(400,160);
		window.getContentPane().setBackground(Color.black);
		window.setVisible(true);

	}

}
