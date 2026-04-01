package project20280.exercises;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class JobPriorityClass {

    private static final int POOL_SIZE = 1;
    private static final int QUEUE_SIZE = 10;

    enum JobPriority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        BACKGROUND
    }

    static class Job implements Runnable {
        private String jobName;
        private JobPriority jobPriority;
        private int expectedDuration;

        public Job(String jobName, JobPriority jobPriority, int expectedDuration) {
            this.jobName = jobName;
            this.jobPriority = jobPriority;
            this.expectedDuration = expectedDuration;
        }

        public String getJobName() {
            return jobName;
        }

        public JobPriority getJobPriority() {
            return jobPriority;
        }

        public int getExpectedDuration() {
            return expectedDuration;
        }

        @Override
        public void run() {
            System.out.println("Running: " + jobName +
                    " | Priority: " + jobPriority +
                    " | Duration: " + expectedDuration + "ms");

            try {
                Thread.sleep(expectedDuration);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class PriorityJobScheduler {
        private ExecutorService workerPool;
        private ExecutorService scheduler;
        private PriorityBlockingQueue<Job> queue;

        public PriorityJobScheduler(int poolSize, int queueSize) {
            workerPool = Executors.newFixedThreadPool(poolSize);
            scheduler = Executors.newSingleThreadExecutor();

            Comparator<Job> comparator =
                    Comparator.comparing(Job::getJobPriority)
                            .thenComparingInt(Job::getExpectedDuration);

            queue = new PriorityBlockingQueue<>(queueSize, comparator);

            scheduler.execute(() -> {
                while (true) {
                    try {
                        workerPool.execute(queue.take());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }

        public void scheduleJob(Job job) {
            queue.add(job);
        }

        public void shutdown() {
            scheduler.shutdownNow();
            workerPool.shutdown();
        }
    }

    public static void main(String[] args) {
        Job job1 = new Job("Job1", JobPriority.LOW, 3000);
        Job job2 = new Job("Job2", JobPriority.MEDIUM, 2000);
        Job job3 = new Job("Job3", JobPriority.HIGH, 4000);
        Job job4 = new Job("Job4", JobPriority.HIGH, 1000);
        Job job5 = new Job("Job5", JobPriority.CRITICAL, 5000);
        Job job6 = new Job("Job6", JobPriority.CRITICAL, 500);

        PriorityJobScheduler scheduler =
                new PriorityJobScheduler(POOL_SIZE, QUEUE_SIZE);

        scheduler.scheduleJob(job1);
        scheduler.scheduleJob(job2);
        scheduler.scheduleJob(job3);
        scheduler.scheduleJob(job4);
        scheduler.scheduleJob(job5);
        scheduler.scheduleJob(job6);

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scheduler.shutdown();
    }
}