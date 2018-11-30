<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
	<xsl:template match="/">
		


		
			
				
				
						<xsl:apply-templates select="customer1"/>
				
				
					
			
	
		
	</xsl:template>
	
	<xsl:template match="customer1">
		<xsl:copy>
			<name>
				<xsl:value-of select="concat(firstName, ' ', lastName)"/>
			</name>
			<fullAddress>
				<xsl:value-of select="concat(address, ', ', city)"/>
			</fullAddress>
			<phone>
				
				<xsl:value-of select="concat('+381','',contactPhone+0)"/>
				
			</phone>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>