"use client";

import { useState } from "react";
import { Heart, ShoppingCart } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { addToCart, toggleFavorite, type Product } from "@/lib/api";
import { toast } from "sonner";
import { mutate } from "swr";

export function ProductCard({ product }: { product: Product }) {
  const [fav, setFav] = useState(product.favorite ?? false);
  const [addingToCart, setAddingToCart] = useState(false);

  async function handleFavorite() {
    try {
      await toggleFavorite(product.id);
      setFav((prev) => !prev);
      toast.success(fav ? "Removed from favorites" : "Added to favorites");
    } catch {
      toast.error("Could not update favorite");
    }
  }

  async function handleAddToCart() {
    setAddingToCart(true);
    try {
      await addToCart(product.id, 1);
      await mutate("cart");
      toast.success("Added to cart");
    } catch {
      toast.error("Could not add to cart");
    } finally {
      setAddingToCart(false);
    }
  }

  return (
    <Card className="group overflow-hidden border border-border bg-card transition-shadow hover:shadow-md">
      <div className="relative aspect-square bg-secondary">
        {product.imageUrl ? (
          <img
            src={product.imageUrl}
            alt={product.title}
            className="h-full w-full object-cover"
          />
        ) : (
          <div className="flex h-full w-full items-center justify-center text-muted-foreground">
            <ShoppingCart className="h-10 w-10 opacity-30" />
          </div>
        )}
        <Button
          variant="ghost"
          size="icon"
          className="absolute right-2 top-2 h-8 w-8 rounded-full bg-card/80 backdrop-blur-sm hover:bg-card"
          onClick={handleFavorite}
          aria-label={fav ? "Remove from favorites" : "Add to favorites"}
        >
          <Heart
            className={`h-4 w-4 transition-colors ${fav ? "fill-accent text-accent" : "text-muted-foreground"}`}
          />
        </Button>
      </div>
      <CardContent className="flex flex-col gap-3 p-4">
        <div className="flex flex-col gap-1">
          {product.category && (
            <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
              {product.category}
            </span>
          )}
          <h3 className="font-medium leading-snug text-foreground">{product.title}</h3>
        </div>
        <div className="flex items-center justify-between">
          <span className="text-lg font-semibold text-foreground">
            ${product.price.toFixed(2)}
          </span>
          <Button
            size="sm"
            onClick={handleAddToCart}
            disabled={addingToCart}
            className="gap-1.5"
          >
            <ShoppingCart className="h-3.5 w-3.5" />
            Add
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}
