#include <bits/stdc++.h>
using namespace std;

//邻接表
//本题需要储存树，输入为节点数量n，根节点编号为x，n-1条无向边(u,v)
const int N = 10010;
unordered_set<int> s;
int h[N], e[N], ne[N], idx;

void add(int a, int b){
	e[idx] = b, ne[idx] = h[a], h[a] = idx++;
}

void dfs(int u,int father,int n){
	s.insert(u);
	for (int i = u; i <=n; i+=u){
		if(s.find(i) != s.end()){ //寻找路径上编号为该节点编号倍数的节点
		//Do something;
		}
	}
	for (int i = h[u]; i != -1; i = ne[i]){
		int v = e[i];
		if(v != father) dfs(v,u,n)
	}
	s.erase(u);
}

int main(){
	memset(h,-1,sizeof h;

	int n,x;
	cin >> n >> x;
	for (int i = 0; i < n -1 ; i++){
		int u,v;
		cin >> u >> v;
		add(u,v);
		add(v,u);
	}
	dfs(4,0,n);
}