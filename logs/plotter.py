import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import pylab
import scipy.stats as stats
import seaborn as sns
import plotly.graph_objects as go

matplotlib.rcParams["axes.formatter.limits"] = (-10, 10)

df = pd.read_csv('log.out', sep=',')

data = df.to_numpy()

last_batch_id = max(data[:, 2])
nodes_count = max(data[:, 1]) + 1
number_of_starts = 5
working_time = 2

number_of_delivered_msg = np.zeros(number_of_starts)
latency_by_start = np.zeros(number_of_starts)

for i in range(number_of_starts):
    indexes_to_keep = np.where(data[:, 0] == i + 1)
    latency_of_start = data[:, 3][indexes_to_keep]
    latency_by_start[i] = np.mean(latency_of_start)
    for j in range(len(data[:, 0])):
        if data[j, 0] == i + 1 and data[j, 1] == nodes_count - 1:
            number_of_delivered_msg[i] = number_of_delivered_msg[i] + 1

number_of_delivered_msg = number_of_delivered_msg // working_time

for el in number_of_delivered_msg:
    print(el)

throughput = np.mean(number_of_delivered_msg)
latency = np.mean(latency_by_start)

print("Throughput: ", round(throughput))
print("Latency: ", round(latency))
print("Median latency: ", round(np.quantile(data[:, 3], 0.5)))

fig = plt.figure()
ax = fig.add_subplot(111)
res = stats.probplot(data[:, 3], dist="norm", plot=ax)
ax.set_title("Распределение latency для всех запусков")
plt.show()

fig, ax = plt.subplots()
plt.figure(figsize=(10, 6))
plt.scatter(range(number_of_starts), number_of_delivered_msg, cmap=plt.cm.rainbow)
ax.plot(range(number_of_starts), number_of_delivered_msg)
ax.grid()
ax.set_xlabel('Номер запуска')
ax.set_ylabel('Throughput (нс)')
ax.set_title("Throughput для всех запусков")
plt.title('Start to work node time')
plt.show()

fig, ax = plt.subplots()
plt.figure(figsize=(10, 6))
plt.scatter(range(number_of_starts), latency_by_start, cmap=plt.cm.rainbow)
ax.plot(range(number_of_starts), latency_by_start)
ax.grid()
ax.set_xlabel('Номер запуска')
ax.set_ylabel('Latency (нс)')
ax.set_title("Latency для всех запусков")
plt.title('Start to work node time')
plt.show()
