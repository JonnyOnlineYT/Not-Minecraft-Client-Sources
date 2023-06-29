package volcano.summer.client.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.base.manager.module.Module;

public class ValueManager {

	public ValueManager() {
	}

	public static ArrayList<Value> values = new ArrayList<Value>();

	public Value getValueUsingName(String name) {
		for (Value value : this.values) {
			if (value.getName().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return null;
	}

	public void addValue(Value value) {
		this.values.add(value);
	}

	public void removeValue(Value value) {
		if (this.values.contains(value)) {
			this.values.remove(value);
		}
	}

	public ArrayList<Value> getValues() {
		return this.values;
	}

	public void organizeValues() {
		Collections.sort(this.values, new Comparator<Value>() {

			@Override
			public int compare(Value value1, Value value2) {
				return value1.getName().compareTo(value2.getName());
			}
		});
		for (Module module : Summer.moduleManager.mods) {
			Summer.commandManager.commands.add(new Command(module.name, "<value>") {
				@Override
				public void run(String message) {
					boolean shouldArgs = false;
					boolean secondCancel = true;
					boolean thirdCancel = true;
					boolean cancel = true;
					boolean say = true;
					String arguments = "Wrong arguments! Try this:";
					String secondArguments = "Wrong arguments! Try this:";
					for (Value value : ValueManager.this.values) {
						if (value instanceof ModeValue) {
							if (module.equals(value.getModule())) {
								ModeValue v = (ModeValue) value;
								say = false;
								cancel = false;
								if (message.split(" ")[1].equals("mode")) {
									if (message.split(" ").length == 3) {
										shouldArgs = true;
										for (int i = 0; i < v.getStringValues().length; i++) {
											if (message.split(" ")[2]
													.equalsIgnoreCase(v.getStringValues()[i].replace("_", ""))) {
												v.setStringValue(v.getStringValues()[i]);
												Summer.tellPlayer("Value "
														+ v.getName().replace(v.getModule().name.toLowerCase(), "")
																.replace("_", "")
														+ " set to " + v.getStringValue().replace("_", "") + ".");
												shouldArgs = false;
												thirdCancel = false;
											}
											secondArguments = secondArguments + " "
													+ v.getStringValues()[i].replace("_", "") + ",";
										}
									} else {
										shouldArgs = false;
										secondCancel = false;
										Summer.tellPlayer(
												"Wrong arguments! " + v.getModule().name + " mode " + "<value>");
									}
								} else {
									shouldArgs = false;
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if ((value.getValue() instanceof Boolean)) {
							if (module.equals(value.getModule())) {
								say = false;
								cancel = false;
								if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
									if (message.split(" ").length == 3) {
										if ((message.split(" ")[2].equalsIgnoreCase("true"))
												|| (message.split(" ")[2].equalsIgnoreCase("false"))
												|| (message.split(" ")[2].equalsIgnoreCase("-d"))) {
											if (message.split(" ")[2].equalsIgnoreCase("-d")) {
												value.setValue(value.getDefaultValue());
											} else {
												value.setValue(
														Boolean.valueOf(Boolean.parseBoolean(message.split(" ")[2])));
											}
											Summer.tellPlayer("Value " + value.getName()
													.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
													+ " set to " + value.getValue() + ".");
											thirdCancel = false;
										} else {
											thirdCancel = false;
											Summer.tellPlayer(
													"Boolean value couldn't be parsed! Check your spelling, this is supposed to be a true/false statement.");
										}
									} else {
										secondCancel = false;
										Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
												+ value.getCommandName() + " <value>");
									}
								} else {
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if (((value.getValue() instanceof Float))
								&& ((value.getValue() instanceof ClampedValue))) {
							if (module.equals(value.getModule())) {
								say = false;
								cancel = false;
								if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
									if (message.split(" ").length == 3) {
										try {
											if (message.split(" ")[2].equalsIgnoreCase("-d")) {
												value.setValue(value.getDefaultValue());
											} else {
												value.setValue(Float.valueOf(Float.parseFloat(message.split(" ")[2])));
											}
											if (((Float) value.getValue())
													.floatValue() > ((Float) ((ClampedValue) value).getMax())
															.floatValue()) {
												value.setValue(((ClampedValue) value).getMax());
											} else if (((Float) value.getValue())
													.floatValue() < ((Float) ((ClampedValue) value).getMin())
															.floatValue()) {
												value.setValue(((ClampedValue) value).getMin());
											}
											Summer.tellPlayer("Value " + value.getName()
													.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
													+ " set to " + value.getValue() + ".");
											thirdCancel = false;
										} catch (Exception e) {
											thirdCancel = false;
											Summer.tellPlayer(
													"Float value couldn't be parsed! Check your spelling, this is supposed to be a number.");
										}
									} else {
										secondCancel = false;
										Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
												+ value.getCommandName() + " <value>");
									}
								} else {
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if (((value.getValue() instanceof Long)) && ((value instanceof ClampedValue))) {
							if (module.equals(value.getModule())) {
								say = false;
								cancel = false;
								if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
									if (message.split(" ").length == 3) {
										try {
											if (message.split(" ")[2].equalsIgnoreCase("-d")) {
												value.setValue(value.getDefaultValue());
											} else {
												value.setValue(Long.valueOf(Long.parseLong(message.split(" ")[2])));
											}
											if (((Long) value.getValue())
													.longValue() > ((Long) ((ClampedValue) value).getMax())
															.longValue()) {
												value.setValue(((ClampedValue) value).getMax());
											} else if (((Long) value.getValue())
													.longValue() < ((Long) ((ClampedValue) value).getMin())
															.longValue()) {
												value.setValue(((ClampedValue) value).getMin());
											}
											Summer.tellPlayer("Value " + value.getName()
													.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
													+ " set to " + value.getValue() + ".");
											thirdCancel = false;
										} catch (Exception e) {
											thirdCancel = false;
											Summer.tellPlayer(
													"Long value couldn't be parsed! Check your spelling, this is supposed to be a number.");
										}
									} else {
										secondCancel = false;
										Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
												+ value.getCommandName() + " <value>");
									}
								} else {
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if (((value.getValue() instanceof Byte)) && ((value instanceof ClampedValue))) {
							if (module.equals(value.getModule())) {
								say = false;
								cancel = false;
								if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
									if (message.split(" ").length == 3) {
										try {
											if (message.split(" ")[2].equalsIgnoreCase("-d")) {
												value.setValue(value.getDefaultValue());
											} else {
												value.setValue(Byte.valueOf(Byte.parseByte(message.split(" ")[2])));
											}
											if (((Byte) value.getValue())
													.byteValue() > ((Byte) ((ClampedValue) value).getMax())
															.byteValue()) {
												value.setValue(((ClampedValue) value).getMax());
											} else if (((Byte) value.getValue())
													.byteValue() < ((Byte) ((ClampedValue) value).getMin())
															.byteValue()) {
												value.setValue(((ClampedValue) value).getMin());
											}
											Summer.tellPlayer("Value " + value.getName()
													.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
													+ " set to " + value.getValue() + ".");
											thirdCancel = false;
										} catch (Exception e) {
											thirdCancel = false;
											Summer.tellPlayer(
													"Byte value couldn't be parsed! Check your spelling, this is supposed to be a number.");
										}
									} else {
										secondCancel = false;
										Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
												+ value.getCommandName() + " <value>");
									}
								} else {
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if (((value.getValue() instanceof Double)) && ((value instanceof ClampedValue))) {
							if (module.equals(value.getModule())) {
								say = false;
								cancel = false;
								if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
									if (message.split(" ").length == 3) {
										try {
											if (message.split(" ")[2].equalsIgnoreCase("-d")) {
												value.setValue(value.getDefaultValue());
											} else {
												value.setValue(
														Double.valueOf(Double.parseDouble(message.split(" ")[2])));
											}
											if (((Double) value.getValue())
													.doubleValue() > ((Double) ((ClampedValue) value).getMax())
															.doubleValue()) {
												value.setValue(((ClampedValue) value).getMax());
											} else if (((Double) value.getValue())
													.doubleValue() < ((Double) ((ClampedValue) value).getMin())
															.doubleValue()) {
												value.setValue(((ClampedValue) value).getMin());
											}
											Summer.tellPlayer("Value " + value.getName()
													.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
													+ " set to " + value.getValue() + ".");
											thirdCancel = false;
										} catch (Exception e) {
											thirdCancel = false;
											Summer.tellPlayer(
													"Double value couldn't be parsed! Check your spelling, this is supposed to be a number.");
										}
									} else {
										secondCancel = false;
										Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
												+ value.getCommandName() + " <value>");
									}
								} else {
									cancel = true;
									arguments = arguments + " " + value.getCommandName() + ",";
								}
							}
						} else if (((value.getValue() instanceof Integer)) && ((value instanceof ClampedValue))
								&& (module.equals(value.getModule()))) {
							say = false;
							cancel = false;
							if (message.split(" ")[1].equalsIgnoreCase(value.getCommandName())) {
								if (message.split(" ").length == 3) {
									try {
										if (message.split(" ")[2].equalsIgnoreCase("-d")) {
											value.setValue(value.getDefaultValue());
										} else {
											value.setValue(Integer.valueOf(Integer.parseInt(message.split(" ")[2])));
										}
										if (((Integer) value.getValue())
												.intValue() > ((Integer) ((ClampedValue) value).getMax()).intValue()) {
											value.setValue(((ClampedValue) value).getMax());
										} else if (((Integer) value.getValue())
												.intValue() < ((Integer) ((ClampedValue) value).getMin()).intValue()) {
											value.setValue(((ClampedValue) value).getMin());
										}
										Summer.tellPlayer("Value " + value.getName()
												.replace(value.getModule().name.toLowerCase(), "").replace("_", "")
												+ " set to " + value.getValue() + ".");
										thirdCancel = false;
									} catch (Exception e) {
										thirdCancel = false;
										Summer.tellPlayer(
												"Integer value couldn't be parsed! Check your spelling, this is supposed to be a number.");
									}
								} else {
									secondCancel = false;
									Summer.tellPlayer("Wrong arguments! " + value.getModule().name + " "
											+ value.getCommandName() + " <value>");
								}
							} else {
								cancel = true;
								arguments = arguments + " " + value.getCommandName() + ",";
							}
						}
					}
					if (say) {
						Summer.tellPlayer("This module doesn't have values.");
					}
					if ((cancel) && (secondCancel) && (thirdCancel) && (!say)) {
						Summer.tellPlayer(arguments.substring(0, arguments.length() - 1) + ".");
					}
					if (shouldArgs) {
						Summer.tellPlayer(secondArguments.substring(0, secondArguments.length() - 1) + ".");
					}
				}
			});
		}

	}

	public ArrayList<Value> getModValues(Module mod) {
		ArrayList<Value> valuess = new ArrayList();
		for (Value v : values) {
			if (v.getModule() == mod) {
				valuess.add(v);
			}
		}
		return valuess;
	}
}
