package me.modmuss50.pong;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * Created by modmuss50 on 19/05/2016.
 */
public class PongClassTransformer implements IClassTransformer {

    String className = "net.minecraft.client.gui.GuiPlayerTabOverlay";
    String obofClassName = "bga";
    String descriptor = "(IIILnet/minecraft/client/network/NetworkPlayerInfo;)V";
    String obofDescriptor = "(IIILbnp;)V";
    String methordName = "drawPing";
    String obofMethordName = "a";


    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals(className) || name.equals(obofClassName))
        {

            FMLLog.log("RebornCore", Level.INFO, String.valueOf("Found " + className));
            ClassNode classNode = readClassFromBytes(bytes);
            MethodNode method = findMethodNodeOfClass(classNode, PongASM.deobfuscatedEnvironment ? methordName : obofMethordName, PongASM.deobfuscatedEnvironment
                    ? descriptor : obofDescriptor);

            method.localVariables = null;
            method.instructions.clear();
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
            method.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
            method.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
            method.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
            method.instructions.add(new MethodInsnNode(
                    Opcodes.INVOKESTATIC, Type.getInternalName(PongHooks.class), methordName, PongASM.deobfuscatedEnvironment
                    ? descriptor : obofDescriptor,
                    false));
            method.instructions.add(new InsnNode(Opcodes.RETURN));
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            bytes = cw.toByteArray();

            return bytes;
        }

        return bytes;
    }

    private ClassNode readClassFromBytes(byte[] bytes)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        return classNode;
    }

    private MethodNode findMethodNodeOfClass(ClassNode classNode, String methodName, String methodDesc)
    {
        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(methodName) && method.desc.equals(methodDesc))
            {
                return method;
            }
        }
        return null;
    }

    public AbstractInsnNode findFirstInstruction(MethodNode method)
    {
        return getOrFindInstruction(method.instructions.getFirst());
    }

    public AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck)
    {
        return getOrFindInstruction(firstInsnToCheck, false);
    }

    public AbstractInsnNode getOrFindInstruction(AbstractInsnNode firstInsnToCheck, boolean reverseDirection)
    {
        for (AbstractInsnNode instruction = firstInsnToCheck; instruction != null; instruction = reverseDirection
                ? instruction.getPrevious() : instruction.getNext())
        {
            if (instruction.getType() != AbstractInsnNode.LABEL && instruction.getType() != AbstractInsnNode.LINE)
                return instruction;
        }
        return null;
    }

    private byte[] writeClassToBytes(ClassNode classNode)
    {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

}
