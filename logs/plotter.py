import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import plotly.graph_objects as go

matplotlib.rcParams["axes.formatter.limits"] = (-10, 10)

df = pd.read_csv('log.out', sep=',')

data = df.to_numpy()

last_batch_id = max(data[:, 1])
nodes_count = data[0, 0] + 1
number_of_starts = len(data[:, 1]) // (last_batch_id + 1)

start_to_batch_time = np.full((number_of_starts, 2), 0)
start_to_node_time = np.full((number_of_starts, 2), 0)

for i in range(number_of_starts):
    start_to_batch_time[i, 0] = i + 1
    start_to_batch_time[i, 1] = np.mean(data[(number_of_starts * i):(number_of_starts * (i + 1)), 2])

for i in range(number_of_starts):
    start_to_node_time[i, 0] = i + 1
    start_to_node_time[i, 1] = data[number_of_starts * (i + 1) - 1, 3]

start_indexes = np.arange(len(data))

for i in range(number_of_starts):
    for j in range(last_batch_id + 1):
        start_indexes[(i * number_of_starts) + j] = i + 1

# Make data.
Y = start_indexes
X = data[:, 1]
Z = data[:, 2]

verts = []
temp_verts = []
for i in range(len(X)):
    temp_verts.append(Z[i])
    if X[i] == last_batch_id:
        verts.append(temp_verts)
        temp_verts = []

fig = go.Figure(data=[go.Surface(x=X[:last_batch_id + 1], z=verts)])
fig.update_layout(scene=dict(
    xaxis_title="Номер пакета",
    yaxis_title="Номер запуска",
    zaxis_title="Время достижения пакета финала (нс)"))
fig.show()

fig, ax = plt.subplots()
plt.figure(figsize=(10, 6))
plt.scatter(start_to_node_time[:, 0], start_to_node_time[:, 1], cmap=plt.cm.rainbow)
ax.plot(start_to_node_time[:, 0], start_to_node_time[:, 1])
ax.grid()
ax.set_xlabel('Номер запуска')
ax.set_ylabel('Время работы (нс)')
plt.title('Start to work node time')
plt.savefig('work.png')
plt.show()

mean_node_work = np.mean(start_to_node_time[:, 1])
ns_to_node = mean_node_work // (last_batch_id + 1)

latency = np.mean(start_to_batch_time[:, 1]) // nodes_count
print("Throughput: ", ns_to_node)
print("Latency: ", latency)
