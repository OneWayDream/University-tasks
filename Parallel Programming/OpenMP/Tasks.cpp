#include <iostream>
#include <omp.h>
#include "windows.h" 

using namespace std;

void task_1();
void task_2(int threads_amount);
void task_3(int a, int b);
void task_4(int a[], int b[]);
void task_5(int d[], int n, int m);
void task_6(int a[], int n);
void task_7();
void task_8();
void test_static();
void test_dynamic();
void test_guided();
void test_runtime();
int* task_9(int arr[], int vektor[]);
void task_10(int d[]);
void task_11(int a[]);
void task_12(int a[]);
void task_13();
void way_1();
void way_2();
void way_3();
void way_4();
void way_5();

int main() {
	/*task_1();*/
	//
	/*task_2(2);
	printf("/-----------------------------\\\n");
	task_2(3);*/
	// 
	/*task_3(1, 2);*/
	//
	/*int a[10], b[10];
	for (int i = 0; i < 10; i++) {
		a[i] = rand() % 100;
		b[i] = rand() % 100;
	}
	task_4(a, b);*/
	//
	/*int d[6][8];
	for (int i = 0; i < 6; i++)
	{
		for (int j = 0; j < 8; j++)
		{
			d[i][j] = rand() % 100;
		}
	}
	task_5(reinterpret_cast<int*>(d), 6, 8);*/
	//
	/*const int n = 100;
	int a[n];
	for (int i = 0; i < n; i++)
	{
		a[i] = rand() % 100;
	}
	task_6(a, n);*/
	// 
	/*task_7();*/
	//
	/*task_8();*/
	//
	/*int arr[1000][100];
	int vektor[100];
	for (int i = 0; i < 1000; i++) {
		for (int j = 0; j < 100; j++) {
			arr[i][j] = rand() % 100;
		}
	}
	for (int i = 0; i < 100; i++) {
		vektor[i] = rand() % 100;
	}
	task_9(reinterpret_cast<int*>(arr), vektor);*/
	//
	/*int d[6][8];
	for (int i = 0; i < 6; i++) {
		for (int j = 0; j < 8; j++) {
			d[i][j] = rand() % 100;
		}
	}
	task_10(reinterpret_cast<int*>(d));*/
	//
	/*int a[30];
	for (int i = 0; i < 30; i++) {
		a[i] = rand() % 100;
	}
	task_11(a);*/
	/*int a[30];
	for (int i = 0; i < 30; i++) {
		a[i] = rand() % 100;
	}
	task_12(a);*/
	/*task_13();*/
	return EXIT_SUCCESS;
}

void task_1() {
	#pragma omp parallel num_threads(8)
	{
		printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
	}
	//Порядок вывода команд произволен - у потоков нет чёткой последовательности выполнения - это случайность.
}

void task_2(int threads_amount) {
	#pragma omp parallel num_threads(threads_amount) if(threads_amount > 2)
	{
		if (omp_in_parallel())
		{
			printf("Thread %d is here.\n", omp_get_thread_num());
		#pragma omp single
			{
				printf_s("val = %d, parallelized with %d threads.\n",
					threads_amount, omp_get_num_threads());
			}
		}
		else
		{
			printf_s("val = %d, serialized\n", threads_amount);
			for (int i = 0; i < threads_amount; i++) {
				printf("Execute %d task\n", i);
			}
		}
	}
	//Программа работает корректно, но количество потоков, которые в итоге задействованы - 16 (для меня).
	//Можно указать их точное количество. Входной параметр влияет лишь на то, будет ли распараллеливание или нет
}

void task_3(int a, int b) {
	printf("a = %d, b = %d;\n", a, b);
	printf("/-----------------------------\\\n");
	#pragma omp parallel num_threads(2) private(a) firstprivate(b)
	{
		int number_of_thread = omp_get_thread_num();
		int a{};

		a += number_of_thread;
		b += number_of_thread;

		printf("a = %d, b = %d, thread_index = %d;\n", a, b, omp_get_thread_num());
	}

	printf("/-----------------------------\\\n");
	printf("a = %d, b = %d;\n", a, b);
	printf("/-----------------------------\\\n");

	#pragma omp parallel num_threads(4) shared(a) private(b)
	{
		int number_of_thread = omp_get_thread_num();
		int b{};

		a -= number_of_thread;
		b -= number_of_thread;

		printf("a = %d, b = %d, thread_index = %d;\n", a, b, omp_get_thread_num());
	}

	printf("/-----------------------------\\\n");
	printf("a = %d, b = %d;\n", a, b);
}

void task_4(int a[], int b[]) {
	#pragma omp single
	{
		printf("a = {");
		for (int i = 0; i < 10; i++)
		{
			printf("%d, ", a[i]);
		}
		printf("}\n");
		printf("b = {");
		for (int i = 0; i < 10; i++)
		{
			printf("%d, ", b[i]);
		}
		printf("}\n");
	}
	#pragma omp parallel num_threads(2)
	{
		if (omp_get_thread_num() == 0) {
			int min = 100;
			for (int i = 0; i < 10; i++)
			{
				if (a[i] < min)
				{
					min = a[i];
				}
			}

			printf("I did minimum task, I'm %d thread!\n", omp_get_thread_num());
			printf("Min value for 'a' array - %d\n", min);
		}
		else
		{
			int max = -1;
			for (int i = 0; i < 10; i++)
			{
				if (b[i] > max)
				{
					max = b[i];
				}
			}

			printf("I did maximum task, I'm %d thread!\n", omp_get_thread_num());
			printf("Max value for 'b' array - %d\n", max);
		}
	}
}

void task_5(int d[], int n, int m) {
	#pragma omp single
	{
		printf("Array d is:\n");
		for (int i = 0; i < n; i++)
		{
			printf("|");
			for (int j = 0; j < m; j++)
			{
				printf("%d", d[i * n + j]);
				if (j < (m - 1))
				{
					printf(", ");
				}
			}
			printf("|\n");
		}
	}
	#pragma omp parallel sections
	{
		#pragma omp section
		{
			int sum = 0;
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < m; j++)
				{
					sum += d[i * n + j];
				}
			}
			float mean = sum / (n * m);
			printf("Hey, I calculated mean value! It's %f. And I'm %d thread.\n", mean, omp_get_thread_num());
		}
		#pragma omp section
		{
			int min = 100;
			int max = -1;
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < m; j++)
				{
					if (d[n * i + j] < min)
					{
						min = d[n * i + j];
					}
					if (d[n * i + j] > max)
					{
						max = d[n * i + j];
					}
				}
			}
			printf("Hey, I calculated maximum and minimum values! It's %d and %d. And I'm %d thread.\n", min, max, omp_get_thread_num());
		}
		#pragma omp section
		{
			int counter = 0;
			for (int i = 0; i < n; i++)
			{
				for (int j = 0; j < m; j++)
				{
					if (d[i * n + j] % 3 == 0)
					{
						counter++;
					}
				}
			}
			printf("Hey, I counted specific element's amount! It's %d. And I'm %d thread.\n", counter, omp_get_thread_num());
		}
	}
}

void task_6(int a[], int n) {
	int sum_for = 0;
	#pragma omp parallel for
	for (int i = 0; i < n; i++)
	{
		sum_for += a[i];
	}
	float mean_for = sum_for / n;
	printf("Mean value is %f, information from 'for' field.\n", mean_for);

	int sum_reduction = 0;
	#pragma omp parallel for reduction(+ : sum_reduction)
	for (int i = 0; i < n; i++)
	{
		sum_reduction += a[i];
	}
	float mean_reduction = sum_reduction / n;
	printf("Mean value is %f, information from 'reduction' field.\n", mean_reduction);
	// Разница результатов - в простом цикле могут происходить потоковые ошибки. А reduction убирает их.
}

void task_7() {
	int a[12], b[12], c[12];

	#pragma omp parallel for num_threads(3) schedule(static, 4)
	for (int i = 0; i < 12; i++) {
		a[i] = rand() % 100;
		b[i] = a[i];
		printf("Hey, I'm thread %d from %d threads, and I generated a[i]=%d and b[i]=%d\n", omp_get_thread_num(), omp_get_num_threads(), a[i], b[i]);
	}

	printf("/-----------------------------\\\n");
	#pragma omp parallel for num_threads(4) schedule(dynamic, 2)
	for (int i = 0; i < 12; i++) {
		c[i] = a[i] + b[i];
		printf("Hey, I'm thread %d from %d threads, and I generated c[i]=%d\n", omp_get_thread_num(), omp_get_num_threads(), c[i]);
	}
}

void task_8() {
	omp_set_num_threads(8);
	test_static();
	test_dynamic();
	test_guided();
	test_runtime();
}

void test_static() {
	printf("Static results (k, time):\n");
	const int n = 16000;
	double best = 1;
	int best_k = 0;
	for (int static_k = 500; static_k <= 1000; static_k++)
	{
		int a[n];
		float b[n];
		double start = omp_get_wtime();
	#pragma omp parallel for schedule(static, static_k)
		for (int i = 0; i < n; i++)
		{
			a[i] = i;
		}
	#pragma omp parallel for schedule(static, static_k)
		for (int i = 1; i < n - 1; i++)
		{
			b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
		}
		double finish = omp_get_wtime();
		double d = finish - start;
		//printf("(%d, %f)\n", static_k, d);
		if (best > d) {
			best = d;
			best_k = static_k;
		}
	}
	printf("Best result is %f with %d koeff\n", best, best_k);
}

void test_dynamic() {
	printf("Dynamic results (k, time):\n");
	const int n = 16000;
	double best = 1;
	int best_k = 0;
	for (int dynamic_k = 300; dynamic_k <= 1000; dynamic_k++)
	{
		int a[n];
		float b[n];
		double start = omp_get_wtime();
	#pragma omp parallel for schedule(dynamic, dynamic_k)
		for (int i = 0; i < n; i++)
		{
			a[i] = i;
		}
	#pragma omp parallel for schedule(dynamic, dynamic_k)
		for (int i = 1; i < n - 1; i++)
		{
			b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
		}
		double finish = omp_get_wtime();
		double d = finish - start;
		//printf("(%d, %f)\n", dynamic_k, d);
		if (best > d) {
			best = d;
			best_k = dynamic_k;
		}
	}
	printf("Best result is %f with %d koeff\n", best, best_k);
}

void test_guided() {
	printf("Guilded results (k, time):\n");
	const int n = 16000;
	double best = 1;
	int best_k = 0;
	for (int guiled_k = 300; guiled_k <= 1000; guiled_k++)
	{
		int a[n];
		float b[n];
		double start = omp_get_wtime();
	#pragma omp parallel for schedule(guided, guiled_k)
		for (int i = 0; i < n; i++)
		{
			a[i] = i;
		}
	#pragma omp parallel for schedule(guided, guiled_k)
		for (int i = 1; i < n - 1; i++)
		{
			b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
		}
		double finish = omp_get_wtime();
		double d = finish - start;
		//printf("(%d, %f)\n", guiled_k, d);
		if (best > d) {
			best = d;
			best_k = guiled_k;
		}
	}
	printf("Best result is %f with %d koeff\n", best, best_k);
}

void test_runtime() {
	printf("Runtime results (time):\n");
	const int n = 16000;
	double start = omp_get_wtime();
	int a[n];
	float b[n];
	#pragma omp parallel for schedule(runtime)
	for (int i = 0; i < n; i++)
	{
		a[i] = i;
	}
	#pragma omp parallel for schedule(runtime)
	for (int i = 1; i < n - 1; i++)
	{
		b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
	}
	double finish = omp_get_wtime();
	double d = finish - start;
	printf("(%f)\n", d);
}

int* task_9(int arr[], int vektor[]) {
	int result[1000];
	double start;
	double finish;
	//loop realisation
	start = omp_get_wtime();
	for (int i = 0; i < 1000; i++) {
		int sum = 0;
		for (int j = 0; j < 100; j++) {
			sum += arr[i * 100 + j] * vektor[j];
		}
		result[i] = sum;
	}
	finish = omp_get_wtime();
	printf("Loop linear method gives us %f time\n", finish - start);

	//static realisation
	start = omp_get_wtime();
	#pragma omp parallel for
	for (int i = 0; i < 1000; i++) {
		int sum = 0;
		for (int j = 0; j < 100; j++) {
			sum += arr[i * 100 + j] * vektor[j];
		}
		result[i] = sum;
	}
	finish = omp_get_wtime();
	printf("static parallel method gives us %f time\n", finish - start);

	//dynamic realisation
	start = omp_get_wtime();
	#pragma omp parallel for schedule(dynamic, 70)
	for (int i = 0; i < 1000; i++) {
		int sum = 0;
		for (int j = 0; j < 100; j++) {
			sum += arr[i * 100 + j] * vektor[j];
		}
		result[i] = sum;
	}
	finish = omp_get_wtime();
	printf("Dynamic parallel method gives us %f time\n", finish - start);

	//guided realisation
	start = omp_get_wtime();
	#pragma omp parallel for schedule(guided, 70)
	for (int i = 0; i < 1000; i++) {
		int sum = 0;
		for (int j = 0; j < 100; j++) {
			sum += arr[i * 100 + j] * vektor[j];
		}
		result[i] = sum;
	}
	finish = omp_get_wtime();
	printf("Guided parallel method gives us %f time\n", finish - start);

	//runtime realisation
	start = omp_get_wtime();
	#pragma omp parallel for schedule(runtime)
	for (int i = 0; i < 1000; i++) {
		int sum = 0;
		for (int j = 0; j < 100; j++) {
			sum += arr[i * 100 + j] * vektor[j];
		}
		result[i] = sum;
	}
	finish = omp_get_wtime();
	printf("Runtime parallel method gives us %f time\n", finish - start);
	return result;
}

void task_10(int d[]) {
	int max = -1;
	int min = 100;
	printf("Array d is:\n");
	for (int i = 0; i < 6; i++) {
		printf("|");
		for (int j = 0; j < 8; j++) {
			printf("%d", d[i * 6 + j]);
			if (j < (8 - 1))
			{
				printf(", ");
			}
		}
		printf("|\n");
	}
	#pragma omp parallel for
	for (int i = 0; i < 6; i++) {
		for (int j = 0; j < 8; j++) {
			{
				if (d[i * 8 + j] > max) {
		#pragma omp critical
					{
						if (d[i * 8 + j] > max)
							max = d[i * 8 + j];
					}
				}
				if (d[i * 8 + j] < min) {
		#pragma omp critical
					{
						if (d[i * 8 + j] < min)
							min = d[i * 8 + j];
					}
				}
			}
		}
	}
	printf("Max = %d, min = %d.", max, min);
}

void task_11(int a[]) {
	int counter = 0;
	printf("Array a: [");
	for (int i = 0; i < 30; i++) {
		printf("%d", a[i]);
		if (i != 29) {
			printf(", ");
		}
	}
	printf("]\n");
	#pragma omp parallel for
	for (int i = 0; i < 30; i++) {
		if (a[i] % 9 == 0) {
	#pragma omp atomic
			counter++;
		}
	}
	printf("%d", counter);
}

void task_12(int a[]) {
	printf("Array a: [");
	for (int i = 0; i < 30; i++) {
		printf("%d", a[i]);
		if (i != 29) {
			printf(", ");
		}
	}
	printf("]\n");
	int max = -1;
	#pragma omp parallel for
	for (int i = 0; i < 30; i++) {
		if (a[i] % 7 == 0) {
			if (a[i] > max) {
		#pragma omp critical
				{
					if (a[i] > max)
						max = a[i];
				}
			}
		}
	}
	printf("Max number aliquot 7 - %d", max);
}

void task_13() {
	way_1();
	//way_2();
	//way_3();
	//way_4();
	//way_5();
}

void way_1() {
	int counter = 0;
	#pragma omp parallel num_threads(8) shared(counter)
	{
		//Sleep((8- omp_get_thread_num())*100);
		int k;
		#pragma omp critical
		{
			printf("Thread num %d\n", omp_get_thread_num());
			counter++;
			k = counter;
			printf("Sleep time %d\n", k);
			printf("------------\n");
		}
		Sleep((8 - k) * 100);
		printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
	}
}

void way_2() {
	#pragma omp parallel sections num_threads(8)
	{
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(700);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(600);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(500);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(400);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section 
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(300);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(200);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(100);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
		#pragma omp section
		{
			printf("%d\n", omp_get_thread_num());
			Sleep(0);
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
	}
}

void way_3() {
	#pragma omp parallel for schedule(static, 1)
	for (int i = 0; i < 8; i++)
	{
		printf("Thread num %d\n", 8 - omp_get_thread_num());
		int k;
		printf("Sleep time %d\n", i);
		printf("------------\n");
		Sleep((8 - i) * 100);
		printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
	}
}

void way_4() {
	#pragma omp parallel num_threads(8)
	{
		for (int i = 7; i > -1; i--)
		{
		#pragma omp barrier
		if (omp_get_thread_num() == i)
			printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
		}
	}
}

void way_5() {
	int counter = 7;
	boolean is_work;
	#pragma omp parallel num_threads(8)
	{
		while (is_work)
		{
			if (counter == omp_get_thread_num())
			{
				printf("Hello world (index = %d, amount of threads = %d)\n", omp_get_thread_num(), omp_get_num_threads());
				counter--;
			}
			if (counter == 0) {
				is_work = false;
			}
		}
	}
}
