
# Process Scheduling Algorithm

This algorithm is based on Non-Preemptive Shortest Job Next algorithm with dynamic priority .It is a scheduling approach that prioritizes jobs based on their estimated run time and the amount of time they have spent waiting. Jobs with longer waiting times are given higher priority, which prevents indefinite postponement. The priority of a job is computed as (1 + waiting time / estimated run time), and the job with the highest priority is scheduled first.

This algorithm has been implemented in a Java program that takes user input dynamically at run time, displays the priority of each process after each unit of time, shows the Gantt chart as output, and calculates the individual waiting time and average waiting time. The program uses a priority queue - for the arrival of processes .It also includes a process class and a process comparator to sort the processes based on priority and arrival time.

This algorithm is useful in situations where there are a large number of jobs with varying estimated run times and waiting times. It ensures that jobs with longer waiting times are given higher priority, which reduces the average waiting time and improves system performance.


## Authors

- [@Manav011](https://www.github.com/Manav011)

