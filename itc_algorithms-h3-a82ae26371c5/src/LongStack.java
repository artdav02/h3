public class LongStack {

   private Node head; //top of the stack //

   private class Node {
      long value;
      Node next;
      Node prev;

      Node(long value) {
         this.value = value;
         this.next = null;
      }


   }
   int size;
   public int size(){
      return size;
   }

   public static void main(String[] argum) {
      // TODO!!! Your tests here!
   }

   LongStack() {
      head = null;
      size=0;
   }

   LongStack(LongStack other) {
      Node otherCurrent = other.head;
      head = null;
      Node current = null;
      while (otherCurrent != null) {
         Node newNode = new Node(otherCurrent.value);
         if (current == null) {
            head = newNode;
            current = newNode;
         } else {
            current.next = newNode;
            current = newNode;
         }
         otherCurrent = otherCurrent.next;
      }
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      return new LongStack(this);
   }

   public boolean stEmpty() {
      return head == null;
   }

   public void push(long a) {
      Node newNode = new Node(a);
      newNode.next = head;
      head = newNode;
      size++;
   }

   public long pop() {
      if (head == null) {
         throw new RuntimeException("Stack underflow");
      }
      long result = head.value;
      head = head.next;
      size--;
      return result;
   } // pop remove

   public void op(String s) {
      long y = pop();
      long x = pop();
      long result;
      switch (s) {
         case "+":
            result = x + y;
            break;
         case "-":
            result = x - y;
            break;
         case "*":
            result = x * y;
            break;
         case "/":
            result = x / y;
            break;
         default:
            throw new RuntimeException("Undefined symbol " + s);
      }
      push(result);
   }

   public long tos() {
      if (head == null) {
         throw new RuntimeException("Stack underflow");
      }
      return head.value;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof LongStack)) {
         return false;
      }
      LongStack other = (LongStack) o;
      Node current = head;
      Node otherCurrent = other.head;
      while (current != null && otherCurrent != null) {
         if (current.value != otherCurrent.value) {
            return false;
         }
         current = current.next;
         otherCurrent = otherCurrent.next;
      }
      return current == null && otherCurrent == null;
   }

   @Override
   // NB append?? //
   public String toString() {
      StringBuilder sb = new StringBuilder();
      Node current = head;
      while (current != null) {
         sb.insert(0, current.value + " ");
         current = current.next;
      }
      return sb.toString().trim();
   }



   public void swap (){
      if (head == null || head.prev == null){
         throw new RuntimeException("Not enough elements in the stack");
      }
      long temp = head.value;
      head.value = head.prev.value;
      head.prev.value = temp;
   }

   public void dup (){
      if (head == null){
         throw new RuntimeException("Not enough elements in the stack");
      }
      push(head.value);
   }

   public static long interpret(String pol) {
      if (pol == null || pol.trim().isEmpty()) {
         throw new RuntimeException("No expression");
      } else if (pol.length() > 1000) {
         throw new RuntimeException("The expression contains too many numbers: " + pol);
      }
      LongStack stack = new LongStack();
      String[] tokens = pol.trim().split("\\s+");
      for (String token : tokens) {
         switch (token) {
            case "+":
            case "-":
            case "*":
            case "/":
               try {
                  stack.op(token);
               } catch (RuntimeException e) {
                  throw new RuntimeException("Error processing operator " + token + " in expression: " + pol, e);
               }
               break;
            case "SWAP":
               if (stack.size < 2){
                  throw new RuntimeException("Not enough elements to perform SWAP" + pol);
               }
               long op2 = stack.pop();
               long op1 = stack.pop();
               stack.push(op2);
               stack.push(op1);
               break;
            case "ROT":
                  if (stack.size() < 3) {
                     throw new RuntimeException("Not enough elements to perform ROT" + pol);
                  }
                  long top = stack.pop();
                  long mid = stack.pop();
                  long bot = stack.pop();
                  stack.push(mid);
                  stack.push(top);
                  stack.push(bot);
               break;
            case "DUP":
               try{
                  stack.dup();
               } catch (RuntimeException e){
                  throw new RuntimeException("Error processing DUP operation" + pol);
               }
               break;
            default:
               try {
                  long value = Long.parseLong(token);
                  stack.push(value);
               } catch (NumberFormatException e) {
                  throw new RuntimeException("Illegal symbol " + token + " in expression: " + pol, e);
               }
               break;
         }
         if (stack.head == null
                 && !token.equals("+")
                 && !token.equals("-")
                 && !token.equals("*")
                 && !token.equals("/")
                 && !token.equals("SWAP")
                 && !token.equals("ROT")
                 && !token.equals("DUP")
         ) {
            throw new RuntimeException("Not enough numbers to perform operation in expression: " + pol);
         }
      }
      if (stack.stEmpty()) {
         throw new RuntimeException("Stack underflow in expression: " + pol);
      }
      long result = stack.pop();
      if (!stack.stEmpty()) {
         throw new RuntimeException("Stack has some elements in it after processing expression: " + pol);
      }
      return result;
   }
}