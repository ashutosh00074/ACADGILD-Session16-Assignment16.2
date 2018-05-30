package com.tech.gigabyte.parallelasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button start_bt;
    TextView tv;
    ProgressBar one_pb, two_pb, three_pb, four_pb, five_pb;
    Task task1, task2, task3, task4, task5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Finding view that was identified by the id attribute from the XML
        */
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv_complete);
        start_bt = (Button) findViewById(R.id.start_bt);
        one_pb = (ProgressBar) findViewById(R.id.one_pb);
        two_pb = (ProgressBar) findViewById(R.id.two_pb);
        three_pb = (ProgressBar) findViewById(R.id.three_pb);
        four_pb = (ProgressBar) findViewById(R.id.four_pb);
        five_pb = (ProgressBar) findViewById(R.id.five_pb);

        start_bt.setOnClickListener(new View.OnClickListener() {

            //Starting AsyncTasks
            @Override
            public void onClick(View view) {
                task1 = new Task(one_pb);
                StartAsyncTaskInParallel(task1);
                task2 = new Task(two_pb);
                StartAsyncTaskInParallel(task2);
                task3 = new Task(three_pb);
                StartAsyncTaskInParallel(task3);
                task4 = new Task(four_pb);
                StartAsyncTaskInParallel(task4);
                task5 = new Task(five_pb);
                StartAsyncTaskInParallel(task5);

            }
        });

    }

    //Executing tasks in parallel --> "THREAD_POOL_EXECUTOR"
    private void StartAsyncTaskInParallel(Task task) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Task extends AsyncTask<Void, Integer, Void> {
        ProgressBar pb;

        Task(ProgressBar pb) {
            this.pb = pb;
        }

        //In Background Process
        @Override
        protected Void doInBackground(Void... voids) {
            int i = 0;
            synchronized (this) {
                while (i < 10) {
                    try {
                        wait(1500);
                        i++;
                        publishProgress(i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        //When Execution Starts
        @Override
        protected void onPreExecute() {
            tv.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Void aVoid) {
            pb.setVisibility(View.GONE);
            tv.setText(R.string.downloads_completed);
            start_bt.setText(R.string.restart);
            super.onPostExecute(aVoid);
        }

        /*
        Updating Progress bar for next --> RESTARTING
        */
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            pb.setVisibility(View.VISIBLE);
            tv.setText(R.string.downloading);
            start_bt.setText(R.string.start);
            pb.setProgress(progress);
            super.onProgressUpdate(values[0]);
        }
    }

}