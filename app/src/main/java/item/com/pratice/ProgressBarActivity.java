package item.com.pratice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class ProgressBarActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;

    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        mProgressBar = findViewById(R.id.progressBar);
        myTask = new MyTask();
        myTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myTask!=null&&myTask.getStatus() == AsyncTask.Status.RUNNING){
            //该方法只是将对应的AsyncTask标记位cancel状态，并不是真正的取消线程的执行。
            myTask.cancel(true);
        }
    }

    class MyTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 100; i++) {
                if(isCancelled()){
                    break;
                }
                publishProgress(i);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(isCancelled()){
                return;
            }
            mProgressBar.setProgress(values[0]);
        }
    }
}
