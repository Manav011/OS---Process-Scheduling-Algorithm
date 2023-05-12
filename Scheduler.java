import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.transform.Source;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Collections;

// this class will only be required to make a comparator for readyQueue so that it can be made a max priority queue
abstract class ProcessComparator implements Comparator<Process> {

}

class Process {
    int pid, arrivaltime, burst, waittime;
    static int currenttime;
    double prio, initialburst;

    // initializing the process
    public Process(int pid, int arrivaltime, int burst) {
        this.pid = pid;
        this.arrivaltime = arrivaltime;
        this.burst = burst;
        this.initialburst = burst;
        this.prio = 0;
        this.waittime = 0; // initial waiting time 0 as it just entered the system
    }

    public double updatedPriority() {
        // priority updating according to the formula
        double res = 1 + waittime / this.initialburst;
        this.prio = Math.round(res * 100.0) / 100.0;
        return this.prio;
    }

    public String toString() {
        if (this.pid == -1)
            return "--";
        return "P" + pid;
    }
}

class Scheduler {

    List<Process> processList, ganttChart, processQueue;
    PriorityQueue<Process> readyQueue;

    public Scheduler() {

        // process submitted by the user but not yet arrived
        processQueue = new ArrayList<>();

        // process that are added to the scheduler
        processList = new ArrayList<>();
        ganttChart = new ArrayList<>();

        // making comparator because ready queue should be max heap
        ProcessComparator priorityr = new ProcessComparator() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.prio != p2.prio) {
                    // Higher priority first
                    return (p2.prio - p1.prio < 0) ? -1 : (p2.prio - p1.prio == 0) ? 0 : 1;
                } else {
                    // Earlier arrival time first
                    return p2.arrivaltime - p1.arrivaltime;
                }
            }
        };

        // process waiting to be scheduled
        readyQueue = new PriorityQueue<>(priorityr);
    }

    // adding the process in queue
    public void addProcess(Process process) {
        processQueue.add(process);
        processList.add(process);
    }

    // calculating average waiting time
    public double calculateAvgWaitingTime() {
        double totalWaitingTime = 0;
        for (Process process : processList) {
            totalWaitingTime += process.waittime;
        }
        return totalWaitingTime / processList.size();
    }

    public void printGantt() {
        System.out.println("\n\nGanttChart :");
        System.out.println("\t\t");
        for (int i = 0; i < ganttChart.size(); i++)
            System.out.print(ganttChart.get(i) + " | ");

        System.out.println("\t\t");
        for (int i = 0; i <= ganttChart.size(); i++)
            if (i < 10)
                System.out.print(i + " | ");
            else
                System.out.println(i + " | ");
    }

    public void run() {
        int currentTime = 0;

        // sorting the processQueue according to arrival time
        Collections.sort(processQueue, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.arrivaltime - p2.arrivaltime;
            }
        });

        while (!processQueue.isEmpty() || !readyQueue.isEmpty()) {
            Process.currenttime = currentTime;
            System.out.println("\nTime " + currentTime + ":");

            // adding the processes in readyQueue
            while (!processQueue.isEmpty() && processQueue.get(0).arrivaltime <= currentTime) {
                Process pr = processQueue.remove(0);
                readyQueue.add(pr);
                System.out.println("\n\t\t" + pr + " arrived at Time : " + currentTime + "\n");
            }

            if (!readyQueue.isEmpty()) {

                // taking the process with highest priority out of the ready queue and adding to
                // Gantt chart
                Process currentProcess = readyQueue.poll();
                ganttChart.add(currentProcess);
                currentProcess.burst--;

                System.out.println("\t\tCurrently running process is : P" + currentProcess.pid + "\n");
                System.out.println("\t\t" + currentProcess + ": priority = " + currentProcess.prio + ", wait time = "
                        + currentProcess.waittime + "s");

                // updating the readyQueue according to the priorities
                {
                    ArrayList<Process> rough = new ArrayList<>();
                    while (!readyQueue.isEmpty()) {
                        Process pr = readyQueue.poll();
                        pr.waittime++;
                        pr.updatedPriority();
                        System.out.println(
                                "\t\t" + pr + ": priority = " + pr.prio + ", wait time = " + pr.waittime + "s");
                        rough.add(pr);
                    }

                    while (!rough.isEmpty()) {
                        readyQueue.add(rough.remove(0));
                    }
                }

                // if current process is not over then add it to readyQueue again
                if (currentProcess.burst > 0) {
                    readyQueue.add(currentProcess);
                }

            } else {
                // when no process is arrived
                System.out.print("\t\tNo process in system");
                ganttChart.add(new Process(-1, -1, -1));
            }

            currentTime++;
        }

        double avgWaitingTime = calculateAvgWaitingTime();
        printGantt();
        System.out.println("\nAverage waiting time: " + avgWaitingTime + "s\n\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Scheduler scheduler = new Scheduler();

        System.out.print("Enter number of processes: \t");
        int n = sc.nextInt();
        System.out.println();

        for (int i = 0; i < n; i++) {

            System.out.print("Enter arrival time for process(in seconds) " + (i + 1) + ": \t");
            int arrivaltime = sc.nextInt();

            System.out.print("Enter burst time for process(in seconds) " + (i + 1) + ": \t");
            int burstTime = sc.nextInt();

            System.out.println();
            Process process = new Process(i + 1, arrivaltime, burstTime);
            scheduler.addProcess(process);
        }

        sc.close();
        scheduler.run();
    }

}
