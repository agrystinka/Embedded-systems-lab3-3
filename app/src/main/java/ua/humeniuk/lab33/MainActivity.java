package ua.humeniuk.lab33;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static int n;
    static int mrt;
    static int mutationCounter;
    static double mut_chanse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText a = (EditText)findViewById(R.id.a);
        final EditText b = (EditText)findViewById(R.id.b);
        final EditText c = (EditText)findViewById(R.id.c);
        final EditText d = (EditText)findViewById(R.id.d);
        final EditText y = (EditText)findViewById(R.id.y);
        final EditText it = (EditText)findViewById(R.id.it);
        Button b1 = (Button)findViewById(R.id.button);
        final TextView tv = (TextView)findViewById(R.id.res);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ia = Integer.parseInt(a.getText().toString());
                int ib = Integer.parseInt(b.getText().toString());
                int ic = Integer.parseInt(c.getText().toString());
                int id = Integer.parseInt(d.getText().toString());
                int iy = Integer.parseInt(y.getText().toString());
                int iit = Integer.parseInt(it.getText().toString());
                n = 0;
                mrt = 0;
                mutationCounter = 0;
                mut_chanse = 0.02;
                int[] rs = Genetic(iy,ia,ib,ic,id,iit);
                tv.setText("Ans : ( "+rs[0]+" ,"+rs[1]+" ,"+rs[2]+" ,"+rs[3]+" )\n"
                        +"Number of iterations: "+n+" out of "+iit+" possible \n"
                        +"Number of mutations: "+mrt+" with mutation chanse " + mut_chanse + "\n"
                        +"Number of mutations is: " + mutationCounter + "\n");
            }
        });
    }

    static int[] Genetic(int res,int a, int b,int c,int d, int itr){
        Random rGen = new Random();
        int [][]iPop = popGen(4,4,res);
        int [][] oGen = new int[4][4];

        int [] done = {0,0,0,0};

        int best_delta = 100000000;
        toploop:
        while(n<itr){
            int [] delta = new int [4];
            double[]dtr = new double[4];
            for(int i = 0;i<4;i++){
                delta[i]=Math.abs(res - (a*iPop[i][0]+b*iPop[i][1]+c*iPop[i][2]+d*iPop[i][3]));

                if(delta[i]==0){
                    done = iPop[i];
                    break toploop;
                }
                if(delta[i]<best_delta) {
                    best_delta = delta[i];
                    done = iPop[i];
                }
                dtr[i]= 1/(double)delta[i];
            }
            double totalDiv = dtr[0]+dtr[1]+dtr[2]+dtr[3];
            double fit = 0;
            int fitI=0;
            for(int i = 0; i<4;i++){
                dtr[i] = dtr[i]/totalDiv;
                if(dtr[i]>fit){
                    fit = dtr[i];
                    fitI = i;
                }
            }



            for(int cnt =0;cnt<4;cnt++) {
                double rnd = rGen.nextDouble();//Math.random();
                if(rnd<=dtr[0]){
                    oGen[cnt] = iPop[0];
                }else if(rnd<=dtr[0]+dtr[1]){
                    oGen[cnt] = iPop[1];
                }else if(rnd<=dtr[0]+dtr[1]+dtr[2]){
                    oGen[cnt] = iPop[2];
                }else{
                    oGen[cnt] = iPop[3];
                }
            }

            iPop = crossOv(oGen,2,2);
            double mutationR = rGen.nextDouble();//Math.random();
            if(mutationR<=mut_chanse) {
                mutate(iPop,res);
                mrt++;
                mutationCounter++;
            }
            n++;
        }
        return done;

    }

    static int[][] crossOv(int[][]toCross,int cNum1,int cNum2){
        int tmp = 0;
        for(int i = 0; i<cNum1;i++){
            tmp = toCross[1][i];
            toCross[1][i] = toCross[0][i];
            toCross[0][i] = tmp;
        }

        for(int i = 0; i<cNum2;i++){

            tmp = toCross[3][i];
            toCross[3][i] = toCross[2][i];
            toCross[2][i]=tmp;
        }
        return toCross;
    }

    static int[][] popGen(int geneN,int popN,int y){
        Random rGen = new Random();
        int [][] pop = new int[popN][geneN];
        for(int i =0;i<popN;i++){
            for(int j = 0; j<geneN;j++){
                pop[i][j]=rGen.nextInt((y/2)+1);//(int) Math.round((Math.random()*(y/2)));
            }
        }

        return pop;
    }

    static void mutate(int[][] mut,int maxBound) {
        Random rGen = new Random();
        int i = rGen.nextInt(4);//(int) (Math.random()*4);
        int j =  rGen.nextInt(4);//(int) (Math.random()*4);
        if(mut[i][j]!=maxBound/2) {
            mut[i][j]++;
        }else {
            mut[i][j]=0;
        }
    }

}
