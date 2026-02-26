package project20280.exercises;

public class Recursion {
    public static int fibonacci(int n) {
        if (n==0) return 0;
        if (n==1) return 1;
        return fibonacci(n-1) + fibonacci(n-2);
    }

    public static int fibonacciMemo(int n) {
        int[] arr = new int[n+1];
        return fibonacciMemoHelper(arr, n);
    }

    private static int fibonacciMemoHelper(int[] arr, int n) {
        if (arr[n] != 0) return arr[n];
        if (n==0) return 0;
        if (n==1) return 1;
        arr[n] = fibonacciMemoHelper(arr, n-1) + fibonacciMemoHelper(arr, n-2);
        return arr[n];
    }

    public static int tribonacci(int n) {
        int[] arr = new int[n+1];
        return tribonacciHelper(arr, n);
    }

    private static int tribonacciHelper(int[] arr, int n) {
        if (arr[n] != 0) return arr[n];
        if (n==0) return 0;
        if (n==1) return 0;
        if (n==2) return 1;
        arr[n] = tribonacciHelper(arr, n-1) + tribonacciHelper(arr, n-2) + tribonacciHelper(arr,n-3);
        return arr[n];
    }

    public static int McCarthy91(int n) {
        if (n > 100) {
            return n - 10;
        } else {
            return McCarthy91(McCarthy91(n + 11));
        }
    }



    public static void main(String[] args) {
        System.out.println(McCarthy91(87));
    }
}
