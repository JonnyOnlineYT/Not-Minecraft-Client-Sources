package me.wintware.client.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import me.wintware.client.event.types.Priority;

public final class EventManager {
   private static final HashMap<Class<? extends Event>, List<EventManager.MethodData>> REGISTRY_MAP = new HashMap();

   public static void register(Object object) {
      Method[] var1 = object.getClass().getDeclaredMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Method method = var1[var3];
         if (!isMethodBad(method)) {
            register(method, object);
         }
      }

   }

   public static void register(Object object, Class<? extends Event> eventClass) {
      Method[] var2 = object.getClass().getDeclaredMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         if (!isMethodBad(method, eventClass)) {
            register(method, object);
         }
      }

   }

   public static void unregister(Object object) {
      Iterator var1 = REGISTRY_MAP.values().iterator();

      while(var1.hasNext()) {
         List<EventManager.MethodData> dataList = (List)var1.next();
         Iterator var3 = dataList.iterator();

         while(var3.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var3.next();
            if (data.getSource().equals(object)) {
               dataList.remove(data);
            }
         }
      }

      cleanMap(true);
   }

   public static void unregister(Object object, Class<? extends Event> eventClass) {
      if (REGISTRY_MAP.containsKey(eventClass)) {
         Iterator var2 = ((List)REGISTRY_MAP.get(eventClass)).iterator();

         while(var2.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var2.next();
            if (data.getSource().equals(object)) {
               ((List)REGISTRY_MAP.get(eventClass)).remove(data);
            }
         }

         cleanMap(true);
      }

   }

   private static void register(Method method, Object object) {
      Class<? extends Event> indexClass = method.getParameterTypes()[0];
      final EventManager.MethodData data = new EventManager.MethodData(object, method, ((EventTarget)method.getAnnotation(EventTarget.class)).value());
      if (!data.getTarget().isAccessible()) {
         data.getTarget().setAccessible(true);
      }

      if (REGISTRY_MAP.containsKey(indexClass)) {
         if (!((List)REGISTRY_MAP.get(indexClass)).contains(data)) {
            ((List)REGISTRY_MAP.get(indexClass)).add(data);
            sortListValue(indexClass);
         }
      } else {
         REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList<EventManager.MethodData>() {
            private static final long serialVersionUID = 666L;

            {
               this.add(data);
            }
         });
      }

   }

   public static void removeEntry(Class<? extends Event> indexClass) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(mapIterator.hasNext()) {
         if (((Class)((Entry)mapIterator.next()).getKey()).equals(indexClass)) {
            mapIterator.remove();
            break;
         }
      }

   }

   public static void cleanMap(boolean onlyEmptyEntries) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if (!mapIterator.hasNext()) {
               return;
            }
         } while(onlyEmptyEntries && !((List)((Entry)mapIterator.next()).getValue()).isEmpty());

         mapIterator.remove();
      }
   }

   private static void sortListValue(Class<? extends Event> indexClass) {
      List<EventManager.MethodData> sortedList = new CopyOnWriteArrayList();
      byte[] var2 = Priority.VALUE_ARRAY;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte priority = var2[var4];
         Iterator var6 = ((List)REGISTRY_MAP.get(indexClass)).iterator();

         while(var6.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var6.next();
            if (data.getPriority() == priority) {
               sortedList.add(data);
            }
         }
      }

      REGISTRY_MAP.put(indexClass, sortedList);
   }

   private static boolean isMethodBad(Method method) {
      return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
      return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static Event call(Event event) {
      List<EventManager.MethodData> dataList = (List)REGISTRY_MAP.get(event.getClass());
      if (dataList != null) {
         if (event instanceof EventStoppable) {
            EventStoppable stoppable = (EventStoppable)event;
            Iterator var3 = dataList.iterator();

            while(var3.hasNext()) {
               EventManager.MethodData data = (EventManager.MethodData)var3.next();
               invoke(data, event);
               if (stoppable.isStopped()) {
                  break;
               }
            }
         } else {
            Iterator var5 = dataList.iterator();

            while(var5.hasNext()) {
               EventManager.MethodData data = (EventManager.MethodData)var5.next();
               invoke(data, event);
            }
         }
      }

      return event;
   }

   private static void invoke(EventManager.MethodData data, Event argument) {
      try {
         data.getTarget().invoke(data.getSource(), argument);
      } catch (Exception var3) {
      }

   }

   private static final class MethodData {
      private final Object source;
      private final Method target;
      private final byte priority;

      public MethodData(Object source, Method target, byte priority) {
         this.source = source;
         this.target = target;
         this.priority = priority;
      }

      public Object getSource() {
         return this.source;
      }

      public Method getTarget() {
         return this.target;
      }

      public byte getPriority() {
         return this.priority;
      }
   }
}
