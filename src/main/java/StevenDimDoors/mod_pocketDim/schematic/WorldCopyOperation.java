package StevenDimDoors.mod_pocketDim.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldCopyOperation extends WorldOperation
{
	private int originX;
	private int originY;
	private int originZ;
	private int index;
	private Block[] blocks;
	private byte[] metadata;
	private NBTTagList tileEntities;
	
	public WorldCopyOperation()
	{
		super("WorldCopyOperation");
		blocks = null;
		metadata = null;
		tileEntities = null;
	}
	
	@Override
	protected boolean initialize(World world, int x, int y, int z, int width, int height, int length)
	{
		index = 0;
		originX = x;
		originY = y;
		originZ = z;
		blocks = new Block[width * height * length];
		metadata = new byte[width * height * length];
		tileEntities = new NBTTagList();
		return true;
	}

	@Override
	protected boolean applyToBlock(World world, int x, int y, int z)
	{
		blocks[index] = world.getBlock(x, y, z);
		metadata[index] = (byte) world.getBlockMetadata(x, y, z);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity != null)
		{
			//Extract tile entity data
			NBTTagCompound tileTag = new NBTTagCompound();
			tileEntity.writeToNBT(tileTag);
			//Translate the tile entity's position from the world's coordinate system
			//to the schematic's coordinate system.
			tileTag.setInteger("x", x - originX);
			tileTag.setInteger("y", y - originY);
			tileTag.setInteger("z", z - originZ);
			tileEntities.appendTag(tileTag);
		}
		index++; //This works assuming the loops in WorldOperation are done in YZX order
		return true;
	}
	
	public Block[] getBlocks()
	{
		return blocks;
	}
	
	public byte[] getMetadata()
	{
		return metadata;
	}
	
	public NBTTagList getTileEntities()
	{
		return tileEntities;
	}
}
