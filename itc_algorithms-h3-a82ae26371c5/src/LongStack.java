public class LongStack {

   private Node head; //top of the stack //

   private class Node {
      long value;
      Node next;

      Node(long value) {
         this.value = value;
         this.next = null;
      }


   }

   public static void main(String[] argum) {
      // TODO!!! Your tests here!
   }

   LongStack() {
      head = null;
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
   }

   public long pop() {
      if (head == null) {
         throw new RuntimeException("Stack underflow");
      }
      long result = head.value;
      head = head.next;
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
            default:
               try {
                  long value = Long.parseLong(token);
                  stack.push(value);
               } catch (NumberFormatException e) {
                  throw new RuntimeException("Illegal symbol " + token + " in expression: " + pol, e);
               }
               break;
         }
         if (stack.head == null && !token.equals("+") && !token.equals("-") && !token.equals("*") && !token.equals("/")) {
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